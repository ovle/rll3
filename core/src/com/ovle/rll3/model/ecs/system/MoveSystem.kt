package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.model.ecs.component.MoveComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.get
import ktx.ashley.get
import kotlin.math.abs


class MoveSystem : IteratingSystem(all(MoveComponent::class.java, PositionComponent::class.java).get()) {
    private val move: ComponentMapper<MoveComponent> = get()
    private val position: ComponentMapper<PositionComponent> = get()
    private val stopDelta = 0.5f

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val moveComponent = entity[move]!!
        if (!moveComponent.started()) return

        move(entity, deltaTime)
    }

    private fun move(entity: Entity, deltaTime: Float) {
        val moveComponent = entity[move]!!
        val positionComponent = entity[position]!!
        val timePercent = moveComponent.tilesPerSecond * deltaTime
        val currentTo = moveComponent.currentTo() ?: return
        val currentPosition = positionComponent.position

        val dx = if (currentTo.x > currentPosition.x) timePercent else -timePercent
        val dy = if (currentTo.y > currentPosition.y) timePercent else -timePercent
        currentPosition.add(dx, dy)

        val distanceToTarget = abs(currentPosition.dst(currentTo))
        val moveFinished = distanceToTarget <= stopDelta
        if (moveFinished) {
            moveComponent.next()
        }
        val pathFinished = moveComponent.finished()
        if (pathFinished) {
            moveComponent.reset()
        }
    }
}