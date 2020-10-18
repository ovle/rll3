package com.ovle.rll3.screen

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.ovle.rll3.event.Event.PlayerControlEvent.*
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.view.centered
import ktx.math.vec2
import ktx.math.vec3

class PlayerControls(private val viewport: Viewport, private val stageViewport: Viewport) : InputAdapter() {

    private var startDragPoint: Vector2? = null
    private var lastDragPoint: Vector2? = null
    private var dragId: Int? = null


    override fun keyUp(keycode: Int) = when (keycode) {
        Input.Keys.MINUS -> { send(CameraScaleDecCommand()); true }
        Input.Keys.PLUS -> { send(CameraScaleIncCommand()); true }
        in (Input.Keys.NUM_1..Input.Keys.NUM_9) -> { send(NumKeyPressedEvent(keycode - 8)); true }
        else -> { send(KeyPressedEvent(keycode)); true }
    }

    override fun scrolled(amount: Int) = send(CameraScrollCommand(amount)).run { true }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        send(
            MouseMovedEvent(
                viewportPoint(screenX, screenY, viewport),
                viewportPoint(screenX, screenY, stageViewport)
            )
        )
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        startDragPoint = null
        lastDragPoint = null
        dragId = null

        send(
            MouseClickEvent(
                viewportPoint(screenX, screenY, viewport),
                viewportPoint(screenX, screenY, stageViewport),
                button
            )
        )

        return true
    }

    // todo main viewport only?
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val viewportPoint = viewportPoint(screenX, screenY, viewport)
        startDragPoint = viewportPoint.cpy()
        lastDragPoint = startDragPoint?.cpy()
        dragId = pointer

        return true
    }

    // todo main viewport only?
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val viewportPoint = viewportPoint(screenX, screenY, viewport)
        send(DragEvent(startDragPoint!!, viewportPoint, lastDragPoint!!.sub(viewportPoint)))
        lastDragPoint = viewportPoint.cpy()

        return true
    }

    private fun viewportPoint(screenX: Int, screenY: Int, viewport: Viewport): Vector2 {
        val x = screenX.toFloat()
        val y = screenY.toFloat()
        return Vector2(x, y).screenToViewport(viewport).centered()
    }

    private fun Vector2.screenToViewport(viewport: Viewport): Vector2 = viewport.unproject(vec3(this)).let { vec2(it.x, it.y) }
}