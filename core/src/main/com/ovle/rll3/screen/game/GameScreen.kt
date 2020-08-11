package com.ovle.rll3.screen.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.Event.PlayerControlEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.eventLogHook
import com.ovle.rll3.model.module.GameModule
import com.ovle.rll3.model.module.interaction.BaseInteractionSystem
import com.ovle.rll3.model.module.interaction.EntityInteractionSystem
import com.ovle.rll3.model.module.skill.SkillSystem
import com.ovle.rll3.model.module.interaction.TileInteractionSystem
import com.ovle.rll3.model.module.render.RenderLevelSystem
import com.ovle.rll3.model.module.render.RenderInteractionInfoSystem
import com.ovle.rll3.model.module.render.RenderObjectsSystem
import com.ovle.rll3.model.module.entityAction.EntityActionSystem
import com.ovle.rll3.model.module.task.TaskSystem
import com.ovle.rll3.model.module.controls.PlayerControlsSystem
import com.ovle.rll3.model.module.game.GameSystem
import com.ovle.rll3.model.module.time.TimeSystem
import com.ovle.rll3.model.module.gathering.ResourceSystem
import com.ovle.rll3.model.module.health.HealthSystem
import com.ovle.rll3.model.module.render.CameraSystem
import com.ovle.rll3.model.module.space.MoveSystem
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.view.initialScale
import com.ovle.rll3.view.scaleScrollCoeff
import ktx.scene2d.table
import sun.reflect.Reflection
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

//      order matters here!
        val systems = listOf(
            PlayerControlsSystem(),

            CameraSystem(camera),
            RenderLevelSystem(camera, assetsManager),
            RenderObjectsSystem(batch, assetsManager),
            RenderInteractionInfoSystem(batch, assetsManager),

            GameSystem(),

            TimeSystem(),
            TaskSystem(),
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

        send(CameraScrolledEvent(((1 - initialScale) / scaleScrollCoeff).roundToInt()))

        send(StartGameCommand())
    }

    override fun hide() {
        super.hide()

        val camera = batchViewport.camera as OrthographicCamera
        send(CameraScrolledEvent(((1 - camera.zoom) / scaleScrollCoeff).roundToInt()))

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