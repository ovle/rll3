package com.ovle.rll3.model.ecs.system
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.entity.focusedEntity
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.view.*
import ktx.ashley.get
import ktx.math.vec3


class CameraSystem(
    private val camera: OrthographicCamera
): EventSystem() {

    private val screenCenter = vec3(screenWidth / 2, screenHeight / 2)

    override fun subscribe() {
        EventBus.subscribe<CameraScaleInc> { onScaleChange(0.1f) }
        EventBus.subscribe<CameraScaleDec> { onScaleChange(-0.1f) }
        EventBus.subscribe<CameraScrolled> { onScaleChange(-it.amount.toFloat() * scaleScrollCoeff) }
        EventBus.subscribe<CameraMoved> { onCameraMoved(it.amount) }

        EventBus.subscribe<EntityInitialized> { onEntityMoved(it.entity) }
        EventBus.subscribe<EntityMoved> { onEntityMoved(it.entity) }

        EventBus.subscribe<EntityFocus> { onEntityFocusEvent(it.entity) }
    }

    private fun onEntityFocusEvent(entity: Entity) {
        val interactionInfo = playerInteractionInfo() ?: return
        with (interactionInfo) {
            focusedEntity = entity
            focusedEntity?.let { focusCamera(it) }
        }
    }

    private fun onEntityMoved(entity: Entity) {
        if (entity != focusedEntity()) return

        focusCamera(entity)
    }

    private fun focusCamera(entity: Entity) {
        val focusedPosition = entity[position]?.gridPosition ?: return
        val focusedWorldPosition = vec3(
            focusedPosition.x * tileWidth.toFloat(),
            focusedPosition.y * tileHeight.toFloat()
        )

        if (focusedWorldPosition.epsilonEquals(camera.position)) return

        camera.position.set(focusedWorldPosition.x, focusedWorldPosition.y, 0.0f)
        camera.update()
    }

    private fun onScaleChange(diff: Float) {
        camera.zoom -= diff
        camera.update()
    }

    private fun onCameraMoved(amount: Vector2) {
        val focusedEntity = focusedEntity()
        if (focusedEntity != null) return

        camera.position.add(amount.x * cameraMoveCoeff, amount.y * cameraMoveCoeff, 0.0f)
        camera.update()
    }
}