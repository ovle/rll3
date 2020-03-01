package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.Event
import com.ovle.rll3.EventBus.send
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.Mappers.move
import com.ovle.rll3.model.ecs.component.Mappers.position
import com.ovle.rll3.model.ecs.component.MoveComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import ktx.ashley.get
import kotlin.math.abs
import kotlin.math.min


class MoveSystem : IteratingSystem(all(MoveComponent::class.java, PositionComponent::class.java).get()) {

    private val stopDelta = 0.25f

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val moveComponent = entity[move]!!
        if (!moveComponent.path.started) return

        val moved = move(entity, deltaTime)
        if (moved) send(Event.EntityMoved(entity))
    }

    //todo move by layer only?
    private fun move(entity: Entity, deltaTime: Float): Boolean {
        val positionComponent = entity[position]!!
        val currentPosition = positionComponent.position

        val moveComponent = entity[move]!!
        val tilesInTime = moveComponent.tilesPerSecond * deltaTime
        val movePath = moveComponent.path
        val currentTarget = movePath.currentTarget ?: return false

        val dx = currentTarget.x - currentPosition.x
        val dy = currentTarget.y - currentPosition.y
        val dxInTimeAbs = min(tilesInTime, abs(dx))
        val dyInTimeAbs = min(tilesInTime, abs(dy))
        val dxInTime = if (dx > 0) dxInTimeAbs else -dxInTimeAbs
        val dyInTime = if (dy > 0) dyInTimeAbs else -dyInTimeAbs

        currentPosition.add(dxInTime, dyInTime)

        val distanceToTarget = abs(currentPosition.dst(floatPoint(currentTarget)))
        val moveFinished = distanceToTarget <= stopDelta
        if (moveFinished) {
            movePath.next()
        }
        val pathFinished = movePath.finished
        if (pathFinished) {
            movePath.reset()
            send(Event.EntityAnimationStopEvent(entity, "walk"))
        }

        return true
    }
}