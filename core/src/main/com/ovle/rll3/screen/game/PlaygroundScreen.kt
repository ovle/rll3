package com.ovle.rll3.screen.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.ovle.rlUtil.gdx.view.PaletteManager
import com.ovle.rll3.assets.AssetsManager
import com.ovle.util.screen.ScreenConfig


class PlaygroundScreen(
    assetsManager: AssetsManager, paletteManager: PaletteManager,
    batch: Batch, camera: OrthographicCamera, screenConfig: ScreenConfig
) : GameScreen(
    assetsManager, paletteManager, batch, camera, screenConfig
)