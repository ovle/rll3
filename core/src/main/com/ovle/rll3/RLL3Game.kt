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
import org.kodein.di.direct
import org.kodein.di.instance


class RLL3Game : KtxGame<BaseScreen>() {

    override fun create() {
        KtxAsync.initiate()

        val directDI = di.direct
        with(directDI) {
            val ls = LoadingScreen(instance(), instance(), instance(), instance())
            val mms = MainMenuScreen(instance(), instance(), instance())
            val ws = WorldScreen(instance(), instance(), instance(), instance(), instance())
            val gs = GameScreen(instance(), instance(), instance(), instance(), instance())
            val pgs = PlaygroundScreen(instance(), instance(), instance(), instance(), instance())

            val screens = listOf(ls, mms, ws, gs, pgs)
            screens.forEach { addScreen(it.javaClass, it) }
        }

        EventBus.subscribe<SwitchScreenCommand> {
            setScreen(it.type, it.payload)
        }

        //todo not displayed 'loading'
        setScreen(LoadingScreen::class.java)

        super.create()
    }

    override fun dispose() {
//        disposables.forEach { it.dispose() }

        super.dispose()
    }

    private fun <Type : BaseScreen> setScreen(type: Class<Type>, payload: Any?) {
        val nextScreen = getScreen(type)
        (nextScreen as BaseScreen).payload = payload

        setScreen(type)
    }
}