package com.ovle.rll3.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Align
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.ScreenManager.ScreenType.*
import ktx.actors.onClick
import ktx.scene2d.label
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup


class MainMenuScreen(screenManager: ScreenManager, batch: Batch, camera: OrthographicCamera): BaseScreen(screenManager, batch, camera) {

    override fun rootActor() =
        verticalGroup {
            label(text = "The game" ) {}

            textButton(text = "Play") {
                onClick { screenManager.goToScreen(GameScreenType) }
            }
            textButton(text = "Manage") {
                onClick { screenManager.goToScreen(ManageScreenType) }
            }
            textButton(text = "Options") {
                onClick { screenManager.goToScreen(OptionsScreenType) }
            }
            textButton(text = "Exit") {
                onClick { Gdx.app.exit() }
            }

            setPosition(batchViewport.screenWidth / 2.0f, batchViewport.screenHeight / 2.0f)
            align(Align.center)
            pack()
        }
}