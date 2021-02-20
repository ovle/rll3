package com.ovle.rll3.model.module.render

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rlUtil.gdx.controls.CameraScaleDecCommand
import com.ovle.rlUtil.gdx.controls.CameraScaleIncCommand
import com.ovle.rlUtil.gdx.controls.CameraScrollCommand
import com.ovle.rlUtil.gdx.controls.DragEvent
import com.ovle.rll3.event.EntityInitializedEvent
import com.ovle.rll3.event.EntityMovedEvent
import com.ovle.rll3.event.FocusEntityCommand
import com.ovle.rll3.model.module.core.entity.focusedEntity
import com.ovle.rll3.model.module.core.entity.playerInteractionInfo
import com.ovle.rll3.model.module.core.entity.positionOrNull
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.interaction.ControlMode
import com.ovle.rll3.view.*
import ktx.math.vec3


class CameraSystem(
    private val camera: Camera
): EventSystem() {

    private val screenCenter = vec3(screenWidth / 2, screenHeight / 2)
    private val focusZoom = 0.5f

    override fun subscribe() {
        subscribe<CameraScaleIncCommand> { onScaleChange(0.1f) }
        subscribe<CameraScaleDecCommand> { onScaleChange(-0.1f) }
        subscribe<CameraScrollCommand> { onScaleChange(-it.amount.toFloat() * scaleScrollCoeff) }
        subscribe<DragEvent> { onCameraMoved(it.lastDiff) }

        subscribe<EntityInitializedEvent> { onEntityMoved(it.entity) }
        subscribe<EntityMovedEvent> { onEntityMoved(it.entity) }

        subscribe<FocusEntityCommand> { onEntityFocusEvent(it.entity) }
    }

    private fun onEntityFocusEvent(entity: Entity) {
        val interactionInfo = engine.playerInteractionInfo() ?: return
        with (interactionInfo) {
            focusedEntity = entity
            focusedEntity?.let { focusCamera(it) }
        }
    }

    private fun onEntityMoved(entity: Entity) {
        if (entity != engine.focusedEntity()) return

        focusCamera(entity)
    }

    private fun onScaleChange(diff: Float) {
        val c = camera
        if (c is OrthographicCamera) {
            c.zoom -= diff
        }
        c.update()

//        println("zoom: ${camera.zoom}")
    }

    private fun onCameraMoved(amount: Vector2) {
        val focusedEntity = engine.focusedEntity()
        if (focusedEntity != null) return

        val interactionInfo = engine.playerInteractionInfo()!!
        if (interactionInfo.controlMode != ControlMode.View) return

        camera.position.add(amount.x * cameraMoveCoeff, amount.y * cameraMoveCoeff, 0.0f)
        camera.update()

//        println("position: ${camera.position}")
    }


    private fun focusCamera(entity: Entity) {
        val position = entity.positionOrNull() ?: return
        focusCamera(position)
    }

    private fun focusCamera(position: GridPoint2) {
        val focusedWorldPosition = vec3(
            position.x * tileSize.toFloat(),
            position.y * tileSize.toFloat()
        )

        if (focusedWorldPosition.epsilonEquals(camera.position)) return
        val c = camera
        c.position.set(focusedWorldPosition.x, focusedWorldPosition.y, 0.0f)
        if (c is OrthographicCamera) {
            c.zoom = focusZoom
        }
        camera.update()
    }
}