package com.ovle.rll3.model.ecs.system.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.AnimationType
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.component.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.Mappers.move
import com.ovle.rll3.model.ecs.component.Mappers.playerInteraction
import com.ovle.rll3.model.ecs.component.Mappers.position
import com.ovle.rll3.model.ecs.component.PlayerInteractionComponent
import com.ovle.rll3.model.ecs.entity.*
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.model.util.entityTilePassMapper
import com.ovle.rll3.model.util.pathfinding.aStar.path
import com.ovle.rll3.model.util.pathfinding.cost
import com.ovle.rll3.model.util.pathfinding.heuristics
import com.ovle.rll3.point
import com.ovle.rll3.toGamePoint
import ktx.ashley.get
import kotlin.math.roundToInt


class PlayerControlsSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<MouseLeftClick> {
            val level = levelInfoNullable() ?: return@subscribe
            onMouseLeftClick(it, level)
        }
        EventBus.subscribe<MouseMoved> {
            val level = levelInfoNullable() ?: return@subscribe
            onMousePositionChange(toGamePoint(it.screenPoint, RenderConfig), level)
        }
    }

    private fun onMouseLeftClick(event: MouseLeftClick, level: LevelInfo) {
        val gamePoint = centered(toGamePoint(event.screenPoint, RenderConfig))

        val connectionEntity = connectionOnPosition(level, point(gamePoint))
        val entities = entitiesOnPosition(level, point(gamePoint))
        when {
            connectionEntity != null -> onTransitionAction(gamePoint, level, connectionEntity)
            entities.isNotEmpty() -> onEntityAction(gamePoint, level, entities)
            else -> onMoveTargetSet(gamePoint, level)
        }
    }

    private fun onEntityAction(gamePoint: Vector2, level: LevelInfo, entities: Collection<Entity>) {
        entities.forEach {
            send(EntityInteractionEvent(it))
        }
    }

    private fun onTransitionAction(gamePoint: Vector2, level: LevelInfo, connectionEntity: Entity) {
        val connectionPositionComponent = connectionEntity[position]
        val playerEntity = playerInteractionInfo()?.controlledEntity ?: return
        val playerPositionComponent = playerEntity[position]!!

        val playerIsNearTransition = true// isNearHV(playerPositionComponent.gridPosition, connectionPositionComponent?.gridPosition)
        if (playerIsNearTransition) {
            val connectionComponent = connectionEntity[levelConnection]!!

            send(EntityLevelTransition(playerEntity, connectionComponent.id))
        }
    }

    //todo at visible point only
    private fun onMoveTargetSet(gamePoint: Vector2, level: LevelInfo) {
        if (!isValid(gamePoint, level)) return

        val playerEntity = playerInteractionInfo()?.controlledEntity ?: return
        val moveComponent = playerEntity[move] ?: return
        val positionComponent = playerEntity[position]!!

        val tiles = level.tiles
        val from = positionComponent.gridPosition
        val to = point(gamePoint)

        val path = path(
            from,
            to,
            tiles,
            obstacles(level),
            heuristicsFn = ::heuristics,
            costFn = ::cost,
            tilePassTypeFn = ::entityTilePassMapper
        )

        if (path.isEmpty()) return

        val movePath = moveComponent.path
        movePath.set(path)

        if (!movePath.started) {
            movePath.start()
//            println("$path")

            send(EntityAnimationStartEvent(playerEntity, AnimationType.Walk))
        }
    }

    //todo center on cursor
    private fun onMousePositionChange(gamePoint: Vector2, level: LevelInfo) {
        println("onMousePositionChange")

        if (!isValid(gamePoint, level)) return

        val interactionEntity = entityWith(allEntities().toList(), PlayerInteractionComponent::class) ?: return
        val positionComponent = interactionEntity[position] ?: return

        positionComponent.position = centered(gamePoint.cpy())

        val entitiesOnPosition = entitiesOnPosition(level, positionComponent.gridPosition) //todo filter interaction itself
        val interactionComponent = interactionEntity[playerInteraction] ?: return
        interactionComponent.hoveredEntities = entitiesOnPosition

//        if (point(gamePoint) == positionComponent.gridPosition) return
//        println(
//            """---------------------
//                entities: ${entitiesOnPosition.print()}
//            """.trimIndent()
//        )
    }

    private fun centered(gamePoint: Vector2) = floatPoint(gamePoint.x - 0.5f, gamePoint.y - 0.5f)

    private fun isValid(gamePoint: Vector2, level: LevelInfo): Boolean {
        return level.tiles.isPointValid(
            gamePoint.x.roundToInt(),
            gamePoint.y.roundToInt()
        )
    }
}
