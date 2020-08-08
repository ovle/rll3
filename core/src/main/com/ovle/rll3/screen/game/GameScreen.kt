package com.ovle.rll3.screen.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.eventLogHook
import com.ovle.rll3.model.ecs.system.*
import com.ovle.rll3.model.ecs.system.interaction.BaseInteractionSystem
import com.ovle.rll3.model.ecs.system.interaction.EntityInteractionSystem
import com.ovle.rll3.model.ecs.system.interaction.SkillSystem
import com.ovle.rll3.model.ecs.system.interaction.TileInteractionSystem
import com.ovle.rll3.model.ecs.system.render.RenderLevelSystem
import com.ovle.rll3.model.ecs.system.render.RenderInteractionInfoSystem
import com.ovle.rll3.model.ecs.system.render.RenderObjectsSystem
import com.ovle.rll3.model.ecs.system.time.ActionSystem
import com.ovle.rll3.model.ecs.system.time.TaskSystem
import com.ovle.rll3.model.ecs.system.time.TimeSystem
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.view.initialScale
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

//      order matters here!
        val systems = listOf(
            PlayerControlsSystem(),

//            GUISystem(stage, screenManager, guiTextureInfo),
            CameraSystem(camera),
            RenderLevelSystem(camera, assetsManager),
            RenderObjectsSystem(batch, assetsManager),
            RenderInteractionInfoSystem(batch, assetsManager),

            LevelSystem(),

            TimeSystem(),
            TaskSystem(),
            ActionSystem(),
            MoveSystem(),
            LivingSystem(),

            BaseInteractionSystem(),
            EntityInteractionSystem(),
            TileInteractionSystem(),

            SkillSystem()

//            CombatSystem(),
//            AnimationSystem(objectsTextureInfo),
//            SightSystem(),
//            ContainerSystem(),
//            AISystem(assetsManager.behaviorTrees),
//            DebugCombatAiSystem(),
//            DebugQuestSystem()
        )

        ecsEngine = PooledEngine()
        systems.forEach { ecsEngine.addSystem((it)) }

        EventBus.addHook(::eventLogHook)

        send(Event.CameraScrolled(((1 - initialScale) / scaleScrollCoeff).roundToInt()))

        send(Event.GameStarted())
    }

    override fun hide() {
        super.hide()

        val camera = batchViewport.camera as OrthographicCamera
        send(Event.CameraScrolled(((1 - camera.zoom) / scaleScrollCoeff).roundToInt()))

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