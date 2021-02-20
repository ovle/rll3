package com.ovle.rll3.screen

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import com.ovle.rlUtil.gdx.view.PaletteManager
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.util.camera
import com.ovle.rll3.util.skin
import com.ovle.rll3.view.palette.paletteOil
import com.ovle.rll3.view.screenHeight
import com.ovle.rll3.view.screenWidth
import com.ovle.util.screen.ScreenConfig
import ktx.scene2d.Scene2DSkin
import org.kodein.di.DI
import org.kodein.di.bind

val screensModule = DI.Module("screens") {
    bind(AssetsManager(disposable(AssetManager())))
    val paletteManager = PaletteManager(paletteOil, paletteOil)
    bind(paletteManager)
    bind(ScreenConfig(screenWidth, screenHeight, paletteManager.bgColor))

    val skin = skin().also {
        Scene2DSkin.defaultSkin = disposable(it)
    }
    bind(skin)
    bind(disposable(SpriteBatch()))
    bind(camera())
}

//todo
private inline fun <reified T : Disposable> disposable(d: T) = d.apply { /*disposables.add(d)*/ }