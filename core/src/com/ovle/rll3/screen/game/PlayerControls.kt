package com.ovle.rll3.screen.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.Event
import com.ovle.rll3.EventBus.send
import com.ovle.rll3.component1
import com.ovle.rll3.component2
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class PlayerControls : InputAdapter() {

    override fun keyUp(keycode: Int) = when (keycode) {
        Input.Keys.MINUS -> {
            send(Event.CameraScaleDec())
            true
        }
        Input.Keys.PLUS -> {
            send(Event.CameraScaleInc())
            true
        }
        else -> false
    }


    override fun scrolled(amount: Int) = send(Event.CameraScrolled(amount)).run { true }

    private var lastDragPoint: Vector2? = null
    private var lastDragId: Int? = null

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        lastDragPoint = null
        lastDragId = null
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val screenPoint = Vector2(screenX.toFloat(), screenY.toFloat())
        lastDragPoint = screenPoint
        lastDragId = pointer
        send(Event.MouseLeftClick(screenPoint))    //todo
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val (oldScreenX, oldScreenY) = lastDragPoint!!
        send(Event.CameraMoved(Vector2(screenX - oldScreenX, screenY - oldScreenY)))
        lastDragPoint!!.set(screenX.toFloat(), screenY.toFloat())
        return true
    }
}


