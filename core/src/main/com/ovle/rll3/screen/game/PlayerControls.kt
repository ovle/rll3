package com.ovle.rll3.screen.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import com.ovle.rll3.event.Event.PlayerControlEvent.*
import com.ovle.rll3.event.EventBus.send
import ktx.math.vec2
import ktx.math.vec3

class PlayerControls(batchViewport: FitViewport) : InputAdapter() {

    private val unproject: ((Vector3) -> Vector3) = batchViewport::unproject
    private var lastDragPoint: Vector2? = null
    private var lastDragId: Int? = null

    override fun keyUp(keycode: Int) = when (keycode) {
        Input.Keys.MINUS -> { send(CameraScaleDecEvent()); true }
        Input.Keys.PLUS -> { send(CameraScaleIncEvent()); true }
        in (Input.Keys.NUM_1..Input.Keys.NUM_9) -> { send(NumKeyPressedEvent(keycode - 8)); true }
        else -> { send(KeyPressedEvent(keycode)); true }
    }

    override fun scrolled(amount: Int) = send(CameraScrolledEvent(amount)).run { true }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        send(MouseMovedEvent(viewportPoint(screenX, screenY)))
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        lastDragPoint = null
        lastDragId = null
        send(MouseClickEvent(viewportPoint(screenX, screenY), button))    //todo left?
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val viewportPoint = viewportPoint(screenX, screenY)
        lastDragPoint = viewportPoint.cpy()
        lastDragId = pointer
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val viewportPoint = viewportPoint(screenX, screenY)
        send(CameraMovedEvent(lastDragPoint!!.sub(viewportPoint)))
        lastDragPoint = viewportPoint.cpy()

        return true
    }

    private fun viewportPoint(screenX: Int, screenY: Int): Vector2 {
        val x = screenX.toFloat()
        val y = screenY.toFloat()
        return Vector2(x, y).screenToViewport()//.centered()
    }

    private fun Vector2.screenToViewport(): Vector2 = unproject(vec3(this)).let { vec2(it.x, it.y) }
}