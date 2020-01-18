package com.ovle.rll3.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.ScreenManager.ScreenType.*
import ktx.actors.onClick
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup
import ktx.scene2d.window


class MainMenuScreen(screenManager: ScreenManager, batch: Batch, assets: AssetManager, camera: OrthographicCamera): BaseScreen(screenManager, batch, assets, camera) {

    override fun rootActor() = window(title = "Menu") {

        verticalGroup {
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
        }

        pack()
    }
}