package com.ovle.rll3.screen.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.component1
import com.ovle.rll3.component2
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.view.centered
import com.ovle.rll3.view.screenToViewport

class PlayerControls : InputAdapter() {

    private var lastDragPoint: Vector2? = null
    private var lastDragId: Int? = null

    override fun keyUp(keycode: Int) = when (keycode) {
        Input.Keys.MINUS -> { send(Event.CameraScaleDec()); true }
        Input.Keys.PLUS -> { send(Event.CameraScaleInc()); true }
        in (Input.Keys.NUM_1..Input.Keys.NUM_9) -> { send(Event.NumKeyPressed(keycode - 8)); true }
        else -> { send(Event.KeyPressed(keycode)); true }
    }

    override fun scrolled(amount: Int) = send(Event.CameraScrolled(amount)).run { true }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        send(Event.MouseMoved(viewportPoint(screenX, screenY)))
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        lastDragPoint = null
        lastDragId = null
        send(Event.MouseClick(viewportPoint(screenX, screenY), button))    //todo left?
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val viewportPoint = viewportPoint(screenX, screenY)
        lastDragPoint = viewportPoint
        lastDragId = pointer
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val (oldScreenX, oldScreenY) = lastDragPoint!!
        send(Event.CameraMoved(Vector2(screenX - oldScreenX, screenY - oldScreenY)))
        lastDragPoint!!.set(screenX.toFloat(), screenY.toFloat())
        return true
    }

    private fun viewportPoint(screenX: Int, screenY: Int): Vector2 {
        val x = screenX.toFloat()
        val y = screenY.toFloat()
        return Vector2(x, y).screenToViewport().centered()
    }
}