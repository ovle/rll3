package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.advanced.ActionComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.move
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.basic.MoveComponent
import com.ovle.rll3.model.ecs.component.basic.PositionComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.action
import com.ovle.rll3.model.ecs.entity.levelInfo
import com.ovle.rll3.model.ecs.entity.obstacles
import com.ovle.rll3.point
import ktx.ashley.get
import kotlin.math.abs
import kotlin.math.min


class TimeSystem : IteratingSystem(all(ActionComponent::class.java).get()) {

    private val ticksInTurn = 100
    private val turnsInSecond = 1

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val actionComponent = entity[action]!!
        with(actionComponent) {
            if (current == null) return
            checkNotNull(timeLeft)

            val deltaTicks = (deltaTime * ticksInTurn * turnsInSecond).toInt()
            timeLeft = timeLeft?.minus(deltaTicks)

            if (timeLeft!! <= 0) {
                current!!.invoke()
                current = null
                timeLeft = null
            }
        }
    }
}