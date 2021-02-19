package com.ovle.rll3

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.ovle.rlUtil.event.EventBus
import com.ovle.rlUtil.gdx.screen.BaseScreen
import com.ovle.rlUtil.gdx.view.PaletteManager
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.event.SwitchScreenCommand
import com.ovle.rll3.screen.LoadingScreen
import com.ovle.rll3.screen.MainMenuScreen
import com.ovle.rll3.screen.WorldScreen
import com.ovle.rll3.screen.game.GameScreen
import com.ovle.rll3.screen.game.PlaygroundScreen
import com.ovle.rll3.view.fontName
import com.ovle.rll3.view.palette.paletteOil
import com.ovle.rll3.view.screenHeight
import com.ovle.rll3.view.screenWidth
import com.ovle.rll3.view.skinPath
import com.ovle.util.screen.ScreenConfig
import ktx.app.KtxGame
import ktx.async.KtxAsync
import ktx.scene2d.Scene2DSkin
import org.kodein.di.DI


class RLL3Game : KtxGame<BaseScreen>() {

    private val disposables = mutableListOf<Disposable>()

    override fun create() {
        KtxAsync.initiate()

        val assetManager = AssetsManager(disposable(AssetManager()))
        val paletteManager = PaletteManager(paletteOil, paletteOil)
        val screenConfig = ScreenConfig(screenWidth, screenHeight, paletteManager.bgColor)
        val skin = Skin(Gdx.files.internal(skinPath))
        val batch = disposable(SpriteBatch())
        val camera = camera()
        skin.getFont(fontName).apply { data.scale(-0.1f) }

        Scene2DSkin.defaultSkin = disposable(skin)

//        val di = DI {
//            bind() from setBinding<String>()
//            bind<String>().inSet() with singleton { "a" }
//            bind<String>().inSet() with singleton { "b" }
//        }
//        val b: Collection<String> by di.instance()

        val ls = LoadingScreen(assetManager, batch, camera, screenConfig)
        val mms = MainMenuScreen(batch, camera, screenConfig)
        val ws = WorldScreen(assetManager, paletteManager, batch, camera, screenConfig)
        val gs = GameScreen(assetManager, paletteManager, batch, camera, screenConfig)
        val pgs = PlaygroundScreen(assetManager, paletteManager, batch, camera, screenConfig)

        val screens = listOf(ls, mms, ws, gs, pgs)
        screens.forEach { addScreen(it.javaClass, it) }

        EventBus.subscribe<SwitchScreenCommand> {
            setScreen(it.type)
            //todo payload
//            if (currentScreen is BaseScreen) {
//                (currentScreen as BaseScreen).payload = it.payload
//            }
        }

        //todo not displayed 'loading'
        setScreen(LoadingScreen::class.java)

        super.create()
    }

    override fun dispose() {
        disposables.forEach { it.dispose() }

        super.dispose()
    }


    private fun camera() =
        OrthographicCamera().apply {
            setToOrtho(false, screenWidth, screenHeight);
            update()
        }

    private inline fun <reified T : Disposable> disposable(d: T) = d.apply { disposables.add(d) }
}