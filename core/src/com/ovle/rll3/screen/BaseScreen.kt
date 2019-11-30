package com.ovle.rll3.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.view.screenHeight
import com.ovle.rll3.view.screenWidth
import ktx.app.KtxScreen


abstract class BaseScreen(
        protected val screenManager: ScreenManager,
        protected val batch: Batch,
        protected val assets: AssetManager,
        protected val camera: OrthographicCamera): KtxScreen {

    private val stage = Stage(FitViewport(screenWidth, screenHeight))
    private var rootActor: Actor? = null

    override fun show() {
        super.show()

        if (rootActor == null) {
            rootActor = rootActor()
            stage.addActor(rootActor)
        }

        val inputMultiplexer = InputMultiplexer(stage)
        screenInputProcessor()?.let { inputMultiplexer.addProcessor(it) }
        Gdx.input.inputProcessor = inputMultiplexer
    }

    open fun screenInputProcessor(): InputProcessor? = null

    override fun hide() {
        super.hide()

        stage.clear()
        rootActor = null
    }

    abstract fun rootActor(): Actor

    override fun render(delta: Float) {
        super.render(delta)

        camera.update()
        batch.projectionMatrix = camera.combined

        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
        super.resize(width, height)
    }

    override fun dispose() {
        stage.dispose()

        super.dispose()
    }
}