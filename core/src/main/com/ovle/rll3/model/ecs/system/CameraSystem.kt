package com.ovle.rll3.model.ecs.system
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.entity.focusedEntity
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.view.scaleScrollCoeff
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth
import ktx.ashley.get


class CameraSystem(
    private val camera: OrthographicCamera
): EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<CameraScaleInc> { onScaleChange(0.1f) }
        EventBus.subscribe<CameraScaleDec> { onScaleChange(-0.1f) }
        EventBus.subscribe<CameraScrolled> { onScaleChange(-it.amount.toFloat() * scaleScrollCoeff) }
        EventBus.subscribe<CameraMoved> { onScrollOffsetChange(it.amount) }

        EventBus.subscribe<EntityInitialized> { onEntityMoved(it.entity) }
        EventBus.subscribe<EntityMoved> { onEntityMoved(it.entity) }

        EventBus.subscribe<DebugToggleFocusEvent> { onDebugToggleFocusEvent() }
    }

    private fun onDebugToggleFocusEvent() {
        val interactionInfo = playerInteractionInfo() ?: return
        with (interactionInfo) {
            focusedEntity = if (focusedEntity != null) null else controlledEntity
            focusedEntity?.let { focusCamera(it) }
        }
    }

    private fun onEntityMoved(entity: Entity) {
        if (entity != focusedEntity()) return

        focusCamera(entity)
    }

    private fun focusCamera(entity: Entity) {
        val focusedPosition = entity[position]?.gridPosition ?: return
        //todo float?
        val focusedScreenPosition = floatPoint(
            focusedPosition.x * tileWidth.toFloat(),
            focusedPosition.y * tileHeight.toFloat()
        )

        if (focusedScreenPosition.epsilonEquals(RenderConfig.scrollOffset)) return

        RenderConfig.scrollOffset = focusedScreenPosition
        camera.position.set(RenderConfig.scrollOffset.x, RenderConfig.scrollOffset.y, 0.0f)
        camera.update()
    }

    private fun onScaleChange(diff: Float) {
        camera.zoom -= diff
        camera.update()
    }

    private fun onScrollOffsetChange(diff: Vector2) {
        val interactionComponent = playerInteractionInfo()
        val focusedEntity = interactionComponent?.focusedEntity
        if (focusedEntity != null) return

        val scrollOffset = RenderConfig.scrollOffset
        scrollOffset.add(-diff.x, diff.y)
        camera.position.set(scrollOffset.x, scrollOffset.y, 0.0f)
        camera.update()
    }
}