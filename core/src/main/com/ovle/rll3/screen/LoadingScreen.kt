package com.ovle.rll3.screen

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Align
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.ScreenManager
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.skipFrame
import ktx.scene2d.label
import ktx.scene2d.verticalGroup

class LoadingScreen(
    val assetsManager: AssetsManager,
    screenManager: ScreenManager, batch: Batch, camera: OrthographicCamera
): BaseScreen(screenManager, batch, camera) {

    override fun show() {
        super.show()

        KtxAsync.launch {
            skipFrame()
            //performs on main thread
            assetsManager.load()
            screenManager.goToScreen(ScreenManager.ScreenType.MainMenuScreenType)
        }
    }

    override fun rootActor() = verticalGroup {
        label(text = "Loading..." ) {}

        setPosition(batchViewport.screenWidth / 2.0f, batchViewport.screenHeight / 2.0f)
        align(Align.center)
        pack()
    }
}