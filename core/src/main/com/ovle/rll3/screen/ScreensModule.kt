package com.ovle.rll3.screen

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Skin
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
import org.kodein.di.provider
import org.kodein.di.singleton

val screensModule = DI.Module("screens") {
    bind<AssetsManager>() with singleton { AssetsManager(disposable(AssetManager()))}

    val paletteManager = PaletteManager(paletteOil, paletteOil)
    bind<PaletteManager>() with singleton { paletteManager }

    bind<ScreenConfig>() with provider { (ScreenConfig(screenWidth, screenHeight, paletteManager.bgColor)) }

    val skin = skin().also {
        Scene2DSkin.defaultSkin = disposable(it)
    }
    bind<Skin>() with singleton { skin }
    bind<SpriteBatch>() with singleton { (disposable(SpriteBatch())) }  //todo singleton ?
    bind<OrthographicCamera>() with singleton { (camera()) }    //todo singleton ?
}

//todo
private inline fun <reified T : Disposable> disposable(d: T) = d.apply { /*disposables.add(d)*/ }