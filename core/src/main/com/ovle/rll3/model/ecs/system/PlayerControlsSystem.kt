package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.Event.*
import com.ovle.rll3.EventBus.receive
import com.ovle.rll3.EventBus.send
import com.ovle.rll3.model.ecs.*
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.component.MoveComponent
import com.ovle.rll3.model.ecs.component.PlayerInteractionComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.model.util.entityTilePassMapper
import com.ovle.rll3.model.util.pathfinding.aStar.path
import com.ovle.rll3.model.util.pathfinding.cost
import com.ovle.rll3.model.util.pathfinding.heuristics
import com.ovle.rll3.point
import com.ovle.rll3.toGamePoint
import ktx.ashley.get
import kotlin.math.roundToInt


class PlayerControlsSystem : EventSystem<PlayerControlEvent>() {
    private val move: ComponentMapper<MoveComponent> = componentMapper()
    private val position: ComponentMapper<PositionComponent> = componentMapper()
    private val interaction: ComponentMapper<PlayerInteractionComponent> = componentMapper()

    override fun channel() = receive<PlayerControlEvent>()

    override fun dispatch(event: PlayerControlEvent) {
        val level = levelInfoNullable() ?: return
        when (event) {
            is MouseLeftClick -> onMoveTargetSet(toGamePoint(event.screenPoint, RenderConfig), level)
            is MouseMoved -> onMousePositionChange(toGamePoint(event.screenPoint, RenderConfig), level)
        }
    }

    private fun onMoveTargetSet(gamePoint: Vector2, level: LevelInfo) {
        if (!isValid(gamePoint, level)) return

        val playerEntity = playerInteractionInfo()?.controlledEntity ?: return
        val moveComponent = playerEntity[move] ?: return
        val positionComponent = playerEntity[position]!!

        val tiles = level.tiles
        val from = positionComponent.gridPosition
        val to = point(gamePoint) ?: return

        val path = path(
            from,
            to,
            tiles,
            heuristicsFn = ::heuristics,
            costFn = ::cost,
            tilePassTypeFn = ::entityTilePassMapper
        )

        val movePath = moveComponent.path
        movePath.set(path)

        if (!movePath.started) {
            movePath.start()
            send(EntityAnimationStartEvent(playerEntity, "walk"))
        }
    }

    private fun onMousePositionChange(gamePoint: Vector2, level: LevelInfo) {
        if (!isValid(gamePoint, level)) return
        val interactionEntity = entityWithNullable(allEntities().toList(), PlayerInteractionComponent::class) ?: return
        val positionComponent = interactionEntity[position] ?: return
        if (point(gamePoint) == positionComponent.gridPosition) return

        val focusedGamePoint = positionComponent.position
        focusedGamePoint.set(gamePoint.x, gamePoint.y)

        val entitiesOnPosition = entitiesOnPosition(level, positionComponent.gridPosition) //todo filter interaction itself
        val interactionComponent = interactionEntity[interaction] ?: return
        interactionComponent.focusedEntities = entitiesOnPosition

        println(
            """---------------------
                entities: ${entitiesOnPosition.print()}                
            """.trimIndent()
        )
    }

    private fun isValid(gamePoint: Vector2, level: LevelInfo): Boolean {
        return level.tiles.isPointValid(
            gamePoint.x.roundToInt(),
            gamePoint.y.roundToInt()
        )
    }
}
