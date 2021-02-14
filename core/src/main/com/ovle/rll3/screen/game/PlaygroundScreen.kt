package com.ovle.rll3.screen.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.ovle.rlUtil.gdx.view.PaletteManager
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.assets.AssetsManager
import com.ovle.util.screen.ScreenConfig
import ktx.inject.Context

class PlaygroundScreen(
    context: Context,
    screenManager: ScreenManager, assetsManager: AssetsManager, paletteManager: PaletteManager,
    batch: Batch, camera: OrthographicCamera, screenConfig: ScreenConfig
) : GameScreen(
    context, screenManager, assetsManager, paletteManager, batch, camera, screenConfig
)