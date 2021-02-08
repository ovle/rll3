package com.ovle.rll3.screen

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Align
import com.ovle.rlUtil.gdx.screen.BaseScreen
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.ScreenManager
import com.ovle.util.screen.ScreenConfig
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.skipFrame
import ktx.scene2d.label
import ktx.scene2d.verticalGroup

class LoadingScreen(
    val assetsManager: AssetsManager,
    val screenManager: ScreenManager,
    batch: Batch, camera: OrthographicCamera, screenConfig: ScreenConfig
): BaseScreen(batch, camera, screenConfig) {

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