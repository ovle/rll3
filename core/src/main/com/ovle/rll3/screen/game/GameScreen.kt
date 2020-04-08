package com.ovle.rll3.screen.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.ovle.rll3.AssetsManager
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.ScreenManager.ScreenType.MainMenuScreenType
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.system.*
import com.ovle.rll3.model.template.EntityTemplatesRegistry
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.view.layer.TexturesInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ktx.actors.onClick
import ktx.scene2d.container
import ktx.scene2d.horizontalGroup
import ktx.scene2d.label
import ktx.scene2d.textButton
import kotlin.math.min


@ExperimentalCoroutinesApi
class GameScreen(
    val assetsManager: AssetsManager,
    screenManager: ScreenManager, batch: Batch, camera: OrthographicCamera
): BaseScreen(screenManager, batch, camera) {

    private lateinit var ecsEngine: PooledEngine
    private val controls = PlayerControls()

    lateinit var infoLabel: Label

    override fun show() {
        super.show()

        val levelTexturesInfo = TexturesInfo(assetsManager.levelTexture)
        val objectsTextureInfo = TexturesInfo(assetsManager.objectsTexture)
        EntityTemplatesRegistry.entityTemplates = assetsManager.templates

        val camera = batchViewport.camera as OrthographicCamera

        val systems = listOf(
            GameSystem(),
            LevelSystem(),
            PlayerControlsSystem(),
            EntityInteractionSystem(),
            MoveSystem(),
            CameraSystem(camera),
            RenderLevelSystem(camera, levelTexturesInfo),
            RenderObjectsSystem(batch, objectsTextureInfo),
            AnimationSystem(objectsTextureInfo),
            SightSystem()
        )

        ecsEngine = PooledEngine()
        systems.forEach { ecsEngine.addSystem((it)) }

        EventBus.send(Event.GameStartedEvent())
    }

    override fun hide() {
        super.hide()

        ecsEngine.clearPools()
        ecsEngine.removeAllEntities()
        ecsEngine.systems.iterator().forEach {
            ecsEngine.removeSystem(it)
        }

        //todo free other resources?
    }

    override fun renderIntr(delta: Float) {
        ecsEngine.update(min(delta, 1 / 60f))
    }


    override fun rootActor() =
        container {
            horizontalGroup {
                textButton(text = "Menu") {
                    align(Align.bottom)
                    onClick { screenManager.goToScreen(MainMenuScreenType) }
                }

                label(text = "text:") {
                    //todo bind to game events
                    infoLabel = this
                }
            }
            pack()
        }


    override fun screenInputProcessor() = controls
}