package com.ovle.rll3.util

import com.badlogic.gdx.graphics.OrthographicCamera
import com.ovle.rll3.view.screenHeight
import com.ovle.rll3.view.screenWidth

fun camera() =
    OrthographicCamera().apply {
        setToOrtho(false, screenWidth, screenHeight);
        update()
    }