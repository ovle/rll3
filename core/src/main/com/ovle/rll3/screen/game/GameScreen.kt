package com.ovle.rll3.screen.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.ScreenManager.ScreenType.MainMenuScreenType
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.system.*
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.spriteTexturePath
import com.ovle.rll3.view.tileTexturePath
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ktx.actors.onClick
import ktx.scene2d.container
import ktx.scene2d.horizontalGroup
import ktx.scene2d.label
import ktx.scene2d.textButton
import kotlin.math.min


@ExperimentalCoroutinesApi
class GameScreen(screenManager: ScreenManager, batch: Batch, assets: AssetManager, camera: OrthographicCamera): BaseScreen(screenManager, batch, assets, camera) {
    lateinit var levelTexture: Texture
    lateinit var objectsTexture: Texture

    private lateinit var ecsEngine: PooledEngine
    private val controls = PlayerControls()

    lateinit var infoLabel: Label

    //todo refactor
    override fun show() {
        super.show()

        //todo async loading on separate screen
        assets.setLoader(Texture::class.java, TextureLoader(InternalFileHandleResolver()))

        assets.load(tileTexturePath, Texture::class.java)
        assets.load(spriteTexturePath, Texture::class.java)
        levelTexture = assets.finishLoadingAsset<Texture>(tileTexturePath)
        objectsTexture = assets.finishLoadingAsset<Texture>(spriteTexturePath)
        val levelTexturesInfo = TexturesInfo(levelTexture)
        val objectsTextureInfo = TexturesInfo(objectsTexture)
        val camera = batchViewport.camera as OrthographicCamera

        ecsEngine = PooledEngine()

        val systems = listOf(
            GameSystem(),
            LevelSystem(),
            PlayerControlsSystem(),
            EntityInteractionSystem(),
            MoveSystem(),
            CameraSystem(camera),
            RenderLevelSystem(camera, levelTexturesInfo),
            RenderObjectsSystem(batch, objectsTextureInfo),
            AnimationSystem(objectsTextureInfo)
        )
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