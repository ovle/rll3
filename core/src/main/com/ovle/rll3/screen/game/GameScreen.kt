package com.ovle.rll3.screen.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.Event.GameEvent.StartGameCommand
import com.ovle.rll3.event.Event.PlayerControlEvent.CameraScrollCommand
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.eventLogHook
import com.ovle.rll3.model.module.ai.AISystem
import com.ovle.rll3.model.module.controls.PlayerControlsSystem
import com.ovle.rll3.model.module.entityAction.EntityActionSystem
import com.ovle.rll3.model.module.game.GameSystem
import com.ovle.rll3.model.module.gathering.ResourceSystem
import com.ovle.rll3.model.module.health.HealthSystem
import com.ovle.rll3.model.module.interaction.BaseInteractionSystem
import com.ovle.rll3.model.module.interaction.EntityInteractionSystem
import com.ovle.rll3.model.module.interaction.TileInteractionSystem
import com.ovle.rll3.model.module.render.*
import com.ovle.rll3.model.module.skill.SkillSystem
import com.ovle.rll3.model.module.space.MoveSystem
import com.ovle.rll3.model.module.task.TaskSystem
import com.ovle.rll3.model.module.time.TimeSystem
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.screen.PlayerControls
import com.ovle.rll3.view.scaleScrollCoeff
import ktx.scene2d.table
import kotlin.math.min
import kotlin.math.roundToInt


class GameScreen(
    private val assetsManager: AssetsManager,
    screenManager: ScreenManager, batch: Batch, camera: OrthographicCamera
) : BaseScreen(screenManager, batch, camera) {

    private lateinit var ecsEngine: PooledEngine
    private val controls = PlayerControls(batchViewport)

    override fun show() {
        super.show()

        val camera = batchViewport.camera as OrthographicCamera
        val gamePayload = payload as InitGameInfo

//      order matters here!
        val systems = listOf(
            PlayerControlsSystem(),

            CameraSystem(camera),
            RenderLevelSystem(camera, assetsManager),
            RenderObjectsSystem(batch, assetsManager),
            RenderInteractionInfoSystem(batch, assetsManager),
            RenderGUISystem(batch, assetsManager, stage.batch),
            AnimationSystem(),

            GameSystem(gamePayload),

            TimeSystem(),
            TaskSystem(),
            AISystem(assetsManager.behaviorTrees),
            EntityActionSystem(),
            MoveSystem(),
            HealthSystem(),

            BaseInteractionSystem(),
            EntityInteractionSystem(),
            TileInteractionSystem(),

            SkillSystem(),
            ResourceSystem()
        )

        ecsEngine = PooledEngine()
        systems.forEach { ecsEngine.addSystem((it)) }

        EventBus.addHook(::eventLogHook)
        EventBus.subscribe<GameDidFinishedEvent> { onGameDidFinishedEvent() }

        camera.zoom = 2.5f
        camera.position.set(500.0f, 500.0f, 0.0f)

        send(StartGameCommand())
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

    override fun rootActor(): Actor {
        return table {
            setFillParent(true)
        }
    }

    override fun screenInputProcessor() = controls
}