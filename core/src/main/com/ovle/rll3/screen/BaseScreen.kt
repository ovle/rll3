package com.ovle.rll3.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.Layout
import com.badlogic.gdx.utils.viewport.FitViewport
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.view.bgColor
import com.ovle.rll3.view.screenHeight
import com.ovle.rll3.view.screenWidth
import ktx.app.KtxScreen
import ktx.app.LetterboxingViewport


abstract class BaseScreen(
    protected val screenManager: ScreenManager,
    protected val batch: Batch,
    camera: OrthographicCamera): KtxScreen {

    protected val stage = Stage(LetterboxingViewport(aspectRatio = screenWidth / screenHeight))
    protected val batchViewport = FitViewport(screenWidth, screenHeight, camera)
    protected var rootActor: Actor? = null

    override fun show() {
        super.show()

        if (rootActor == null) {
            rootActor = rootActor()
            stage.addActor(rootActor)
        }

        val inputMultiplexer = InputMultiplexer(stage)
        screenInputProcessor()?.let { inputMultiplexer.addProcessor(it) }
        Gdx.input.inputProcessor = inputMultiplexer

        RenderConfig.unproject = batchViewport::unproject
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

        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        //sprite batch
        batchViewport.apply()
        batch.projectionMatrix = batchViewport.camera.combined
        renderIntr(delta)

        //stage
        stage.viewport.apply()
        stage.act(delta)
        stage.draw()
    }

    open fun renderIntr(delta: Float) {}

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)

        batchViewport.update(width, height)
        stage.viewport.update(width, height, true)

        if (rootActor is Layout) {
            (rootActor as Layout).invalidate()
        }
    }

    override fun dispose() {
        stage.dispose() //todo?

        super.dispose()
    }
}