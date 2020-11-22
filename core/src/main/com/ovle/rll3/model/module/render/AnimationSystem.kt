package com.ovle.rll3.model.module.render
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.component.ComponentMappers.render
import com.ovle.rll3.model.module.render.Animation.*
import com.ovle.rll3.model.util.Direction
import ktx.ashley.get
import java.lang.IllegalArgumentException


class AnimationSystem: IteratingSystem(Family.all(RenderComponent::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val renderComponent = entity[render]!!
        val animation = renderComponent.currentAnimation
        animation?.let {
            it.time = it.time.plus(deltaTime)
            val totalLength = animation.animation.totalLength

            if (it.time > totalLength) it.time = it.time % totalLength
        }
    }

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        subscribe()
    }

    fun subscribe() {
        EventBus.subscribe<EntityStartedMoveEvent> { onEntityStartedMoveEvent(it.entity) }
        EventBus.subscribe<EntityFinishedMoveEvent> { onEntityFinishedMoveEvent(it.entity) }

        EventBus.subscribe<EntityStartUseSkillEvent> { onEntityStartUseSkillEvent(it.source, it.target) }
        EventBus.subscribe<EntityFinishUseSkillEvent> { onEntityFinishUseSkillEvent(it.source, it.target) }
    }

    private fun onEntityStartUseSkillEvent(source: Entity, target: Any?) {
        val sourcePosition = source[position]!!.gridPosition
        val targetPosition = when (target) {
            is Entity -> target[position]!!.gridPosition
            is GridPoint2 -> target
            else -> throw IllegalArgumentException("todo")
        }

        val direction = if (sourcePosition.x != targetPosition.x) Direction.H else Direction.V  //todo both?
        val isPositive = if (direction == Direction.V) sourcePosition.y < targetPosition.y
            else sourcePosition.x < targetPosition.x

        val sourceAnimation = ShiftAnimation(
                direction = direction,
                frames = arrayOf(0, 1, 2, 3, 4, 2).map { if (isPositive) it else -it }.toTypedArray(),
                frameLength = 0.1f
            )
        val targetAnimation = BlinkAnimation(
            blinkRegion = null,
            frameLength = 0.25f
        )

        source.setAnimation(sourceAnimation)

        if (target is Entity) {
            target[render]!!.currentAnimation = AnimationInfo(targetAnimation)
        }
    }

    private fun onEntityFinishUseSkillEvent(source: Entity, target: Any?) {
        source.setAnimation(null)
        if (target is Entity) {
            target.setAnimation(null)
        }
    }

    private fun onEntityStartedMoveEvent(entity: Entity) {
        val animation = ShiftAnimation(
            direction = Direction.V,
            frames = arrayOf(0, 1, 2, 1),
            frameLength = 0.25f
        )
        entity.setAnimation(animation)
    }

    private fun onEntityFinishedMoveEvent(entity: Entity) {
        entity.setAnimation(null)
    }

    private fun Entity.setAnimation(animation: Animation?) {
        this[render]!!.currentAnimation = if (animation == null) null else AnimationInfo(animation)
    }
}