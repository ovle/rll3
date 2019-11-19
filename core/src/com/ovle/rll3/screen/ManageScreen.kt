package com.ovle.rll3.screen

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.ScreenManager.ScreenType.MainMenuScreenType
import ktx.actors.onClick
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup
import ktx.scene2d.window


class ManageScreen(screenManager: ScreenManager, batch: Batch, assets: AssetManager, camera: OrthographicCamera): BaseScreen(screenManager, batch, assets, camera) {

    override fun rootActor() = window(title = "Manage") {

        verticalGroup {
            textButton(text = "Back") {
                onClick { screenManager.goToScreen(MainMenuScreenType) }
            }
        }

        pack()
    }
}