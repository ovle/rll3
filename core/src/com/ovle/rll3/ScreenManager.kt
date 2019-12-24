package com.ovle.rll3

import com.badlogic.gdx.utils.Disposable
import com.ovle.rll3.ScreenManager.ScreenType.*
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.screen.MainMenuScreen
import com.ovle.rll3.screen.ManageScreen
import com.ovle.rll3.screen.game.GameScreen
import ktx.inject.Context


class ScreenManager(private val context: Context, val setScreen: (BaseScreen) -> Unit): Disposable {

    enum class ScreenType {
        MainMenuScreenType,
        ManageScreenType,
        GameScreenType,
        OptionsScreenType
    }

    private val screensByType by lazy {
        context.run {
            mapOf(
                    MainMenuScreenType to MainMenuScreen(inject(), inject(), inject(), inject()),
                    ManageScreenType to ManageScreen(inject(), inject(), inject(), inject()),
                    GameScreenType to GameScreen(inject(), inject(), inject(), inject())
            )
        }
    }

    override fun dispose() {
        screens().forEach{ it.dispose() }
    }

    fun screens() = screensByType.values
    fun initScreen() = MainMenuScreen::class.java
    fun goToScreen(screenType: ScreenType) = setScreen(screensByType[screenType]
            ?: error("screen $screenType not found"))
}