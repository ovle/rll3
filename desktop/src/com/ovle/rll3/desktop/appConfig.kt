package com.ovle.rll3.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.ovle.rlUtil.gdx.view.PaletteManager
import com.ovle.rll3.view.palette.paletteOil
import com.ovle.rll3.view.screenHeight
import com.ovle.rll3.view.screenWidth
import kotlin.math.roundToInt

val appConfig = LwjglApplicationConfiguration().apply {
    fullscreen = false
    width = screenWidth.roundToInt()
    height = screenHeight.roundToInt()

    initialBackgroundColor = PaletteManager(paletteOil, paletteOil).bgColor
}
