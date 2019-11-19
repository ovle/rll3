package com.ovle.rll3

import com.ovle.rll3.ScreenManager.ScreenType.*
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.screen.GameScreen
import com.ovle.rll3.screen.MainMenuScreen
import com.ovle.rll3.screen.ManageScreen
import ktx.inject.Context


class ScreenManager(private val context: Context, val setScreen: (BaseScreen) -> Unit) {

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

    fun screens() = screensByType.values
    fun initScreen() = MainMenuScreen::class.java
    fun goToScreen(screenType: ScreenType) = setScreen(screensByType[screenType]
            ?: error("screen $screenType not found"))
}