package com.ovle.rll3.model.ecs.system

import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.util.Mappers.playerInteraction
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.entity.controlledEntity
import com.ovle.rll3.model.ecs.entity.entitiesOnPosition
import com.ovle.rll3.model.ecs.entity.levelInfoNullable
import com.ovle.rll3.model.ecs.entity.playerInteraction
import com.ovle.rll3.model.ecs.see
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.point
import com.ovle.rll3.toGamePoint
import ktx.ashley.get


class PlayerControlsSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<MouseClick> {
            onMouseClick(it, it.button)
        }
        EventBus.subscribe<MouseMoved> {
            onMousePositionChange(toGamePoint(it.screenPoint, RenderConfig))
        }
    }

    private fun onMouseClick(event: MouseClick, button: Int) {
        val level = levelInfoNullable() ?: return

        val gamePoint = centered(toGamePoint(event.screenPoint, RenderConfig))
        val position = point(gamePoint)

        val controlledEntity = controlledEntity()
        if (controlledEntity?.see(position) == false) return

        val entities = entitiesOnPosition(level, position)
        when {
            entities.isNotEmpty() -> {
                val target = entities.single()
                send(EntityClick(button, target))
            }
            else -> {
                send(VoidClick(button, position))
            }
        }
    }

    private fun onMousePositionChange(gamePoint: Vector2) {
        val level = levelInfoNullable() ?: return

        val point = point(centered(gamePoint.cpy()))
        val tiles = level.tiles
        if (!tiles.isPointValid(point.x, point.y)) return

        val interactionEntity = playerInteraction() ?: return
        val positionComponent = interactionEntity[position]!!

        if (positionComponent.gridPosition == point) return

        positionComponent.gridPosition = point

        val controlledEntity = controlledEntity()
        if (controlledEntity?.see(positionComponent.gridPosition) == false) return

        val entitiesOnPosition = entitiesOnPosition(level, positionComponent.gridPosition) //todo filter interaction itself
        val interactionComponent = interactionEntity[playerInteraction] ?: return
        interactionComponent.hoveredEntities = entitiesOnPosition

        send(EntityUnhoverEvent())
        entitiesOnPosition.forEach {
            send(EntityHoverEvent(it))
        }
    }

    private fun centered(gamePoint: Vector2) = floatPoint(gamePoint.x - 0.5f, gamePoint.y - 0.5f)
}
