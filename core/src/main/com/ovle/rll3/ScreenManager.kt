package com.ovle.rll3

import com.badlogic.gdx.utils.Disposable
import com.ovle.rll3.ScreenManager.ScreenType.*
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.screen.LoadingScreen
import com.ovle.rll3.screen.MainMenuScreen
import com.ovle.rll3.screen.game.GameScreen
import ktx.inject.Context


class ScreenManager(private val context: Context, val setScreen: (BaseScreen) -> Unit): Disposable {

    enum class ScreenType {
        LoadingScreenType,
        MainMenuScreenType,
        GameScreenType,
        OptionsScreenType
    }

    private val screensByType by lazy {
        context.run {
            mapOf(
                    LoadingScreenType to LoadingScreen(inject(), inject(), inject(), inject()),
                    MainMenuScreenType to MainMenuScreen(inject(), inject(), inject()),
                    GameScreenType to GameScreen(inject(), inject(), inject(), inject())
            )
        }
    }

    override fun dispose() {
        screens().forEach{ it.dispose() }
    }

    fun screens() = screensByType.values
    fun initScreen() = LoadingScreen::class.java
    fun goToScreen(screenType: ScreenType) = setScreen(screensByType[screenType] ?: error("screen $screenType not found"))
}