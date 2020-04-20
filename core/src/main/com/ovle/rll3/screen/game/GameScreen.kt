package com.ovle.rll3.screen.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.ovle.rll3.AssetsManager
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.ScreenManager.ScreenType.MainMenuScreenType
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.eventLogHook
import com.ovle.rll3.model.ecs.system.*
import com.ovle.rll3.model.ecs.system.interaction.CombatSystem
import com.ovle.rll3.model.ecs.system.interaction.EntityInteractionSystem
import com.ovle.rll3.model.ecs.system.level.LevelRegistry
import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.view.layer.TexturesInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ktx.actors.onClick
import ktx.scene2d.table
import ktx.scene2d.textButton
import kotlin.math.min


@ExperimentalCoroutinesApi
class GameScreen(
    private val assetsManager: AssetsManager,
    screenManager: ScreenManager, batch: Batch, camera: OrthographicCamera
): BaseScreen(screenManager, batch, camera) {

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

            GameSystem(),
            LevelSystem(),
            EntityInteractionSystem(),
            CombatSystem(),
            MoveSystem(),
            AnimationSystem(objectsTextureInfo),
            SightSystem()
        )

        ecsEngine = PooledEngine()
        systems.forEach { ecsEngine.addSystem((it)) }

        EventBus.addHook(::eventLogHook)

        EventBus.send(Event.GameStartedEvent())
    }

    override fun hide() {
        super.hide()

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

    override fun rootActor() =
        table {
//            debug()
            setFillParent(true)

            bottom()
            textButton(text = "Menu") {
                onClick { screenManager.goToScreen(MainMenuScreenType) }
            }
            pack()
        }

    override fun screenInputProcessor() = controls

// todo fix camera issue (corrupt of gamePoint evaluation).
// todo this is weird
    override fun resize(width: Int, height: Int) {
        batchViewport.update(width, height)
        stage.viewport.update(width, height)
    }
}