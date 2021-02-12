package com.ovle.rll3.view.game

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.ovle.rlUtil.gdx.view.PaletteManager
import com.ovle.rll3.assets.AssetsManager

class GameView(
    private val assetsManager: AssetsManager,
    private val paletteManager: PaletteManager,
    private val batch: Batch,
    private val stageBatch: Batch,
    private val camera: OrthographicCamera,
    private val engine: Engine
) {
    private val renderers = listOf(
        GUIRenderer(batch, stageBatch, assetsManager, paletteManager, engine)
    )

    fun render(deltaTime: Float) {
        renderers.forEach { it.render(deltaTime) }
    }
}