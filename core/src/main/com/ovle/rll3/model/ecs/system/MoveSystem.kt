package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.Event
import com.ovle.rll3.EventBus.send
import com.ovle.rll3.model.ecs.component.MoveComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.componentMapper
import ktx.ashley.get
import kotlin.math.abs
import kotlin.math.roundToInt


class MoveSystem : IteratingSystem(all(MoveComponent::class.java, PositionComponent::class.java).get()) {
    private val move: ComponentMapper<MoveComponent> = componentMapper()
    private val position: ComponentMapper<PositionComponent> = componentMapper()
    private val stopDelta = 0.25f
    private val minMoveDistance = 0.25f

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val moveComponent = entity[move]!!
        if (!moveComponent.path.started()) return

        val moved = move(entity, deltaTime)
        if (moved) send(Event.EntityMoved(entity))
    }

    //todo move by layer only?
    private fun move(entity: Entity, deltaTime: Float): Boolean {
        val moveComponent = entity[move]!!
        val positionComponent = entity[position]!!
        val timePercent = moveComponent.tilesPerSecond * deltaTime
        val movePath = moveComponent.path
        val currentTo = movePath.currentTo() ?: return false
        val currentPosition = positionComponent.position

        val dx = if (currentTo.x > currentPosition.x) timePercent else -timePercent
        val dy = if (currentTo.y > currentPosition.y) timePercent else -timePercent
//        if (dx < minMoveDistance && dy < minMoveDistance) return false  //todo sqr dst ?

        currentPosition.add(dx.roundToInt(), dy.roundToInt())

        val distanceToTarget = abs(currentPosition.dst(currentTo))
        val moveFinished = distanceToTarget <= stopDelta
        if (moveFinished) {
            movePath.next()
        }
        val pathFinished = movePath.finished()
        if (pathFinished) {
            movePath.reset()
        }

        return true
    }
}