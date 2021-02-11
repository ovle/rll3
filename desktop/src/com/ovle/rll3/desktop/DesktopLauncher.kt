package com.ovle.rll3.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.ovle.rll3.RLL3Game

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        LwjglApplication(RLL3Game(), appConfig)
    }
}
