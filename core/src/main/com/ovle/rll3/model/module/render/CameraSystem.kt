package com.ovle.rll3.model.module.render

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.Event.PlayerControlEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.entity.focusedEntity
import com.ovle.rll3.model.module.core.entity.playerInteractionInfo
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.interaction.ControlMode
import com.ovle.rll3.view.*
import ktx.ashley.get
import ktx.math.vec3


class CameraSystem(
    private val camera: OrthographicCamera
): EventSystem() {

    private val screenCenter = vec3(screenWidth / 2, screenHeight / 2)
    private val focusZoom = 0.5f

    override fun subscribe() {
        EventBus.subscribe<CameraScaleIncCommand> { onScaleChange(0.1f) }
        EventBus.subscribe<CameraScaleDecCommand> { onScaleChange(-0.1f) }
        EventBus.subscribe<CameraScrollCommand> { onScaleChange(-it.amount.toFloat() * scaleScrollCoeff) }
        EventBus.subscribe<DragEvent> { onCameraMoved(it.lastDiff) }

        EventBus.subscribe<EntityInitializedEvent> { onEntityMoved(it.entity) }
        EventBus.subscribe<EntityMovedEvent> { onEntityMoved(it.entity) }

        EventBus.subscribe<FocusEntityCommand> { onEntityFocusEvent(it.entity) }
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

    private fun onScaleChange(diff: Float) {
        camera.zoom -= diff
        camera.update()

//        println("zoom: ${camera.zoom}")
    }

    private fun onCameraMoved(amount: Vector2) {
        val focusedEntity = focusedEntity()
        if (focusedEntity != null) return

        val interactionInfo = playerInteractionInfo()!!
        if (interactionInfo.controlMode != ControlMode.View) return

        camera.position.add(amount.x * cameraMoveCoeff, amount.y * cameraMoveCoeff, 0.0f)
        camera.update()

//        println("position: ${camera.position}")
    }


    private fun focusCamera(entity: Entity) {
        val position = entity[position]?.gridPosition ?: return
        focusCamera(position)
    }

    private fun focusCamera(position: GridPoint2) {
        val focusedWorldPosition = vec3(
            position.x * tileWidth.toFloat(),
            position.y * tileHeight.toFloat()
        )

        if (focusedWorldPosition.epsilonEquals(camera.position)) return

        camera.position.set(focusedWorldPosition.x, focusedWorldPosition.y, 0.0f)
        camera.zoom = focusZoom
        camera.update()
    }
}