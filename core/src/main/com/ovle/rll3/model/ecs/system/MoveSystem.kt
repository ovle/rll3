package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.basic.MoveComponent
import com.ovle.rll3.model.ecs.component.basic.PositionComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.action
import com.ovle.rll3.model.ecs.component.util.Mappers.move
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.entity.levelInfo
import com.ovle.rll3.model.ecs.entity.obstacles
import com.ovle.rll3.point
import ktx.ashley.get
import kotlin.math.abs


class MoveSystem : IteratingSystem(all(MoveComponent::class.java, PositionComponent::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val moveComponent = entity[move]!!
        if (!moveComponent.path.started) return

        checkMove(entity)
    }

    private fun checkMove(entity: Entity) {
        val positionComponent = entity[position]!!
        val moveComponent = entity[move]!!
        val movePath = moveComponent.path
        val currentTarget = movePath.currentTarget ?: return

        val gridPosition = positionComponent.gridPosition
        val moveFinished = currentTarget == gridPosition
        if (moveFinished) {
            movePath.next()
        }
        val pathFinished = movePath.finished
        if (pathFinished) {
            movePath.reset()
            send(Event.EntityFinishMove(entity))
        } else {
            val actionComponent = entity[action]!!
            if (actionComponent.current == null) {
                actionComponent.current = { move(entity) }
                actionComponent.timeLeft = moveComponent.ticksPerTile
            }
        }
    }

    private fun move(entity: Entity) {
        val positionComponent = entity[position]!!
        val moveComponent = entity[move]!!

        val currentPosition = positionComponent.gridPosition
        val currentTarget = moveComponent.path.currentTarget ?: return

        val dx = currentTarget.x - currentPosition.x
        val dy = currentTarget.y - currentPosition.y
        val useX = abs(dx) > abs(dy)
        val dxStep = when {
            !useX -> 0
            dx > 0 -> 1
            dx == 0 -> 0
            else -> -1
        }
        val dyStep = when {
            useX -> 0
            dy > 0 -> 1
            dy == 0 -> 0
            else -> -1
        }

        val newPosition = point(currentPosition.x + dxStep, currentPosition.y + dyStep)
        //it can be expensive to do that on every move processing. cache?
        val obstacles = obstacles(levelInfo())
        //should we finish move at that point, or consider obstacle to be temporary? or try to make another path?
        if (newPosition in obstacles) return

        positionComponent.gridPosition = newPosition

        send(Event.EntityMoved(entity))
    }
}