package com.ovle.rll3.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.ovle.rll3.RLL3Game
import com.ovle.rll3.view.gameFullscreen
import com.ovle.rll3.view.palette.Palette.bgColor
import com.ovle.rll3.view.screenHeight
import com.ovle.rll3.view.screenWidth
import kotlin.math.roundToInt

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            fullscreen = gameFullscreen
            width = screenWidth.roundToInt()
            height = screenHeight.roundToInt()

            initialBackgroundColor = bgColor
        }

        LwjglApplication(RLL3Game(), config)
    }
}
