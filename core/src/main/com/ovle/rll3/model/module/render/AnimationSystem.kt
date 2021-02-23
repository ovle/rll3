package com.ovle.rll3.model.module.render

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rll3.ExactTurn
import com.ovle.rll3.model.util.renderEntities
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.skill.SkillUsage
import com.ovle.rlUtil.gdx.math.Direction
import com.ovle.rlUtil.gdx.view.Animation
import com.ovle.rlUtil.gdx.view.AnimationInfo
import com.ovle.rll3.event.*
import com.ovle.rll3.model.module.render.Components.render
import com.ovle.rll3.model.module.space.position
import ktx.ashley.get

class AnimationSystem: EventSystem() {

    override fun subscribe() {
        subscribe<TimeChangedEvent> { onTimeChangedEvent(it) }

        subscribe<EntityStartedMoveEvent> { onEntityStartedMoveEvent(it.entity) }
        subscribe<EntityFinishedMoveEvent> { onEntityFinishedMoveEvent(it.entity) }

        subscribe<EntityStartUseSkillEvent> { onEntityStartUseSkillEvent(it.info) }
        subscribe<EntityFinishUseSkillEvent> { onEntityFinishUseSkillEvent(it.info) }

        subscribe<EntityDiedEvent> { onEntityDiedEvent(it.entity) }
    }

    private fun onTimeChangedEvent(event: TimeChangedEvent) {
        val entities = engine.renderEntities()
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

        val sourceAnimation = Animation.ShiftAnimation(
            direction = direction,
            frames = arrayOf(0, 1, 2, 3, 4, 2).map { if (isPositive) it else -it }.toTypedArray(),
            frameLength = 0.1
        )
        val targetAnimation = Animation.BlinkAnimation(
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
        val animation = Animation.ShiftAnimation(
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