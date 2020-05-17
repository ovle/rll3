package com.ovle.rll3.screen.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.ovle.rll3.AssetsManager
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.eventLogHook
import com.ovle.rll3.model.ecs.system.*
import com.ovle.rll3.model.ecs.system.gui.GUISystem
import com.ovle.rll3.model.ecs.system.interaction.CombatSystem
import com.ovle.rll3.model.ecs.system.interaction.DebugCombatAiSystem
import com.ovle.rll3.model.ecs.system.interaction.EntityInteractionSystem
import com.ovle.rll3.model.ecs.system.level.LevelRegistry
import com.ovle.rll3.model.ecs.system.render.RenderInteractionInfoSystem
import com.ovle.rll3.model.ecs.system.render.RenderLevelSystem
import com.ovle.rll3.model.ecs.system.render.RenderObjectsSystem
import com.ovle.rll3.model.ecs.system.time.ActionSystem
import com.ovle.rll3.model.ecs.system.time.TimeSystem
import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.view.initialScale
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.scaleScrollCoeff
import ktx.scene2d.table
import kotlin.math.min
import kotlin.math.roundToInt


class GameScreen(
    private val assetsManager: AssetsManager,
    screenManager: ScreenManager, batch: Batch, camera: OrthographicCamera
) : BaseScreen(screenManager, batch, camera) {

    private lateinit var ecsEngine: PooledEngine
    private val controls = PlayerControls()

    override fun show() {
        super.show()

        val levelTexturesInfo = TexturesInfo(assetsManager.levelTexture)
        val objectsTextureInfo = TexturesInfo(assetsManager.objectsTexture)
        TemplatesRegistry.entityTemplates = assetsManager.entityTemplates
        TemplatesRegistry.structureTemplates = assetsManager.structureTemplates

        val camera = batchViewport.camera as OrthographicCamera

        val systems = listOf(
            GUISystem(stage, assetsManager.guiTexture),
            PlayerControlsSystem(),
            CameraSystem(camera),
            RenderLevelSystem(camera, levelTexturesInfo),
            RenderObjectsSystem(batch, objectsTextureInfo),
            RenderInteractionInfoSystem(batch, assetsManager.guiTexture),
            GameSystem(),
            LevelSystem(),
            TimeSystem(),
            ActionSystem(),
            EntityInteractionSystem(),
            CombatSystem(),
            MoveSystem(),
            AnimationSystem(objectsTextureInfo),
            SightSystem(),
            ContainerSystem(),

            DebugCombatAiSystem()
        )

        ecsEngine = PooledEngine()
        systems.forEach { ecsEngine.addSystem((it)) }

        EventBus.addHook(::eventLogHook)

        send(Event.CameraScrolled(((1 - initialScale) / scaleScrollCoeff).roundToInt()))

        send(Event.GameStartedEvent())
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

        LevelRegistry.clear()
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