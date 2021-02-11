package com.ovle.rll3.screen.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.ovle.rlUtil.event.EventBus
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rlUtil.gdx.controls.CameraScrollCommand
import com.ovle.rlUtil.gdx.controls.PlayerControls
import com.ovle.rlUtil.gdx.screen.BaseScreen
import com.ovle.rlUtil.gdx.view.PaletteManager
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.event.GameDidFinishedEvent
import com.ovle.rll3.event.StartGameCommand
import com.ovle.rll3.event.eventLogHook
import com.ovle.rll3.model.module.core.system.SystemManager
import com.ovle.rll3.view.scaleScrollCoeff
import com.ovle.util.screen.ScreenConfig
import ktx.scene2d.scene2d
import ktx.scene2d.table
import kotlin.math.min
import kotlin.math.roundToInt


class GameScreen(
    private val screenManager: ScreenManager,
    assetsManager: AssetsManager,
    paletteManager: PaletteManager,
    batch: Batch, camera: OrthographicCamera, screenConfig: ScreenConfig
) : BaseScreen(batch, camera, screenConfig) {

    private lateinit var ecsEngine: PooledEngine
    private val controls = PlayerControls(batchViewport)
    private val systemManager = SystemManager(
        assetsManager,
        paletteManager,
        batch,
        stage.batch,
        camera
    )


    override fun show() {
        super.show()

        val camera = batchViewport.camera as OrthographicCamera
        val systems = systemManager.systems()

        ecsEngine = PooledEngine()
        systems.forEach { ecsEngine.addSystem((it)) }

        EventBus.addHook(::eventLogHook)
        EventBus.subscribe<GameDidFinishedEvent> { onGameDidFinishedEvent() }

        camera.apply {
            zoom = 2.5f
            position.set(500.0f, 500.0f, 0.0f)
        }

        send(StartGameCommand(payload as InitGameInfo))
    }

    private fun onGameDidFinishedEvent() {
        screenManager.goToScreen(ScreenManager.ScreenType.WorldScreenType)
    }

    override fun hide() {
        super.hide()

        val camera = batchViewport.camera as OrthographicCamera
        send(CameraScrollCommand(((1 - camera.zoom) / scaleScrollCoeff).roundToInt()))

        ecsEngine.clearPools()
        ecsEngine.removeAllEntities()
        ecsEngine.systems.iterator().forEach {
            ecsEngine.removeSystem(it)
        }

        EventBus.clearSubscriptions()
        EventBus.clearHooks()

        //todo free other resources?
    }

    override fun renderIntr(delta: Float) {
        ecsEngine.update(min(delta, 1 / 60f))
    }

    override fun rootActor(): Actor =
        scene2d.table {
            setFillParent(true)
        }

    override fun screenInputProcessor() = controls
}