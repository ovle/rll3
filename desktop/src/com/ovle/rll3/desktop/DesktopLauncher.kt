package com.ovle.rll3.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.ovle.rll3.RLL3Game
import com.ovle.rll3.view.gameFullscreen

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            fullscreen = gameFullscreen
//            initialBackgroundColor =
        }
        LwjglApplication(RLL3Game(), config)
    }
}
