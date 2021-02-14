package com.ovle.rll3

import com.badlogic.gdx.utils.Disposable
import com.ovle.rll3.ScreenManager.ScreenType.*
import com.ovle.rll3.screen.LoadingScreen
import com.ovle.rll3.screen.MainMenuScreen
import com.ovle.rll3.screen.WorldScreen
import com.ovle.rll3.screen.game.GameScreen
import com.ovle.rll3.screen.game.PlaygroundScreen
import ktx.inject.Context


class ScreenManager(private val context: Context, val setScreen: SetScreen): Disposable {

    enum class ScreenType {
        LoadingScreenType,
        MainMenuScreenType,
        WorldScreenType,
        PlaygroundScreenType,
        GameScreenType,
        OptionsScreenType
    }

    private val screensByType by lazy {
        context.run {
            mapOf(
                    LoadingScreenType to LoadingScreen(inject(), inject(), inject(), inject(), inject()),
                    MainMenuScreenType to MainMenuScreen(inject(), inject(), inject(), inject()),
                    WorldScreenType to WorldScreen(inject(), inject(), inject(), inject(), inject(), inject()),
                    GameScreenType to GameScreen(context, inject(), inject(), inject(), inject(), inject(), inject()),
                    PlaygroundScreenType to PlaygroundScreen(context, inject(), inject(), inject(), inject(), inject(), inject())
            )
        }
    }

    override fun dispose() {
        screens().forEach{ it.dispose() }
    }

    fun screens() = screensByType.values
    fun initScreen() = LoadingScreen::class.java
    fun goToScreen(screenType: ScreenType, payload: Any? = null) =
        setScreen(
            screensByType[screenType] ?: error("screen $screenType not found"),
            payload
        )
}