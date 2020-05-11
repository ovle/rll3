package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.ecs.component.special.PlayerInteractionComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.move
import com.ovle.rll3.model.ecs.component.util.Mappers.playerInteraction
import com.ovle.rll3.model.ecs.component.util.Mappers.position
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
        EventBus.subscribe<MouseClick> {
            val level = levelInfoNullable() ?: return@subscribe
            when(it.button) {
                Input.Buttons.LEFT -> onMouseLeftClick(it, level)
                Input.Buttons.RIGHT -> onMouseRightClick(it, level)
            }
        }
        EventBus.subscribe<MouseMoved> {
            val level = levelInfoNullable() ?: return@subscribe
            onMousePositionChange(toGamePoint(it.screenPoint, RenderConfig), level)
        }
    }

    private fun onMouseLeftClick(event: MouseClick, level: LevelInfo) {
        val gamePoint = centered(toGamePoint(event.screenPoint, RenderConfig))
        val entities = entitiesOnPosition(level, point(gamePoint))

        if (entities.isEmpty()) send(EntityDeselectEvent())
        when {
            entities.isNotEmpty() -> onEntityAction(gamePoint, level, entities) //todo default action
            else -> {
                val playerEntity = playerInteractionInfo()?.controlledEntity ?: return
                send(EntityMoveTargetSet(point(gamePoint), playerEntity))
            }
        }
    }

    private fun onMouseRightClick(event: MouseClick, level: LevelInfo) {
        //todo
    }

    private fun onEntityAction(gamePoint: Vector2, level: LevelInfo, entities: Collection<Entity>) {
        entities.forEach {
            send(EntitySelectEvent(it))
        }
    }

    private fun onMousePositionChange(gamePoint: Vector2, level: LevelInfo) {
        val point = point(centered(gamePoint.cpy()))
        val tiles = level.tiles
        if (!tiles.isPointValid(point.x, point.y)) return

        val interactionEntity = entityWith(allEntities().toList(), PlayerInteractionComponent::class) ?: return
        val positionComponent = interactionEntity[position] ?: return

        positionComponent.gridPosition = point

        val entitiesOnPosition = entitiesOnPosition(level, positionComponent.gridPosition) //todo filter interaction itself
        val interactionComponent = interactionEntity[playerInteraction] ?: return
        interactionComponent.hoveredEntities = entitiesOnPosition
    }

    private fun centered(gamePoint: Vector2) = floatPoint(gamePoint.x - 0.5f, gamePoint.y - 0.5f)
}
