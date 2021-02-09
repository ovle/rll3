package com.ovle.rll3.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Align
import com.ovle.rlUtil.gdx.screen.BaseScreen
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.ScreenManager.ScreenType.*
import com.ovle.util.screen.ScreenConfig
import ktx.actors.onClick
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup


class MainMenuScreen(
    private val screenManager: ScreenManager,
    batch: Batch,
    camera: OrthographicCamera,
    screenConfig: ScreenConfig
) : BaseScreen(
    batch,
    camera,
    screenConfig
) {

    override fun rootActor() =
        scene2d.verticalGroup {
            label(text = "The game") {}

            textButton(text = "Play") {
                onClick { screenManager.goToScreen(WorldScreenType) }
            }
            textButton(text = "Options") {
                onClick { screenManager.goToScreen(OptionsScreenType) }
            }
            textButton(text = "Exit") {
                onClick { Gdx.app.exit() }
            }

            setFillParent(true)
            align(Align.center)
            pack()
        }
}