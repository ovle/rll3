package com.ovle.rll3.model.module.render

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.ExactTurn
import com.ovle.rll3.Turn
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.render
import com.ovle.rll3.model.module.core.entity.position
import com.ovle.rll3.model.module.core.entity.renderEntities
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.render.Animation.BlinkAnimation
import com.ovle.rll3.model.module.render.Animation.ShiftAnimation
import com.ovle.rll3.model.module.skill.SkillUsage
import com.ovle.rll3.model.util.Direction
import ktx.ashley.get

class AnimationSystem: EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<TimeChangedEvent> { onTimeChangedEvent(it) }

        EventBus.subscribe<EntityStartedMoveEvent> { onEntityStartedMoveEvent(it.entity) }
        EventBus.subscribe<EntityFinishedMoveEvent> { onEntityFinishedMoveEvent(it.entity) }

        EventBus.subscribe<EntityStartUseSkillEvent> { onEntityStartUseSkillEvent(it.info) }
        EventBus.subscribe<EntityFinishUseSkillEvent> { onEntityFinishUseSkillEvent(it.info) }

        EventBus.subscribe<EntityDiedEvent> { onEntityDiedEvent(it.entity) }
    }

    private fun onTimeChangedEvent(event: TimeChangedEvent) {
        val entities = renderEntities()
        entities.forEach { processEntity(it, event.exactDeltaTurns) }
    }

    private fun processEntity(entity: Entity, exactDeltaTurns: ExactTurn) {
        val renderComponent = entity[render]!!
        val animation = renderComponent.currentAnimation
        animation?.let {
            it.time = it.time.plus(exactDeltaTurns)
            val totalLength = animation.animation.totalLength

            if (it.time > totalLength) it.time = it.time % totalLength
        }
    }

    private fun onEntityStartUseSkillEvent(info: SkillUsage) {
        val (_, source, target, _) = info

        val sourcePosition = source.position()
        val targetPosition = when (target) {
            is Entity -> target.position()
            is GridPoint2 -> target
            else -> throw IllegalArgumentException("todo")
        }

        val direction = if (sourcePosition.x != targetPosition.x) Direction.H else Direction.V  //todo both?
        val isPositive = if (direction == Direction.V) sourcePosition.y < targetPosition.y
            else sourcePosition.x < targetPosition.x

        val sourceAnimation = ShiftAnimation(
                direction = direction,
                frames = arrayOf(0, 1, 2, 3, 4, 2).map { if (isPositive) it else -it }.toTypedArray(),
                frameLength = 0.1
            )
        val targetAnimation = BlinkAnimation(
            blinkRegion = null,
            frameLength = 0.25
        )

        source.setAnimation(sourceAnimation)

        if (target is Entity) {
            target[render]!!.currentAnimation = AnimationInfo(targetAnimation)
        }
    }

    private fun onEntityFinishUseSkillEvent(info: SkillUsage) {
        val (_, source, target, _) = info
        source.setAnimation(null)
        if (target is Entity) {
            target.setAnimation(null)
        }
    }

    private fun onEntityStartedMoveEvent(entity: Entity) {
        val animation = ShiftAnimation(
            direction = Direction.V,
            frames = arrayOf(0, 1, 2, 1),
            frameLength = 0.25
        )
        entity.setAnimation(animation)
    }

    private fun onEntityFinishedMoveEvent(entity: Entity) {
        entity.setAnimation(null)
    }

    private fun onEntityDiedEvent(entity: Entity) {
        entity.setAnimation(null)
    }

    private fun Entity.setAnimation(animation: Animation?) {
        this[render]!!.currentAnimation = if (animation == null) null else AnimationInfo(animation)
    }
}