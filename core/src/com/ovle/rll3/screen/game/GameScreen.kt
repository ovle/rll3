package com.ovle.rll3.screen.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.ovle.rll3.Event
import com.ovle.rll3.EventBus
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.ScreenManager.ScreenType.MainMenuScreenType
import com.ovle.rll3.model.ecs.system.*
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.view.spriteTexturePath
import com.ovle.rll3.view.tileTexturePath
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ktx.actors.onClick
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup
import ktx.scene2d.window
import kotlin.math.min


@ExperimentalCoroutinesApi
class GameScreen(screenManager: ScreenManager, batch: Batch, assets: AssetManager, camera: OrthographicCamera): BaseScreen(screenManager, batch, assets, camera) {
    lateinit var texture: Texture
    lateinit var spriteTexture: Texture

    private lateinit var ecsEngine: PooledEngine
    private val controls = PlayerControls()

    //todo refactor
    override fun show() {
        super.show()

        //todo async loading on separate screen
        assets.setLoader(Texture::class.java, TextureLoader(InternalFileHandleResolver()))

        assets.load(tileTexturePath, Texture::class.java)
        assets.load(spriteTexturePath, Texture::class.java)
        texture = assets.finishLoadingAsset<Texture>(tileTexturePath)
        spriteTexture = assets.finishLoadingAsset<Texture>(spriteTexturePath)

        ecsEngine = PooledEngine()

        val levelSystem = LevelSystem()
        val animationSystem = AnimationSystem()
        val moveSystem = MoveSystem()
        val playerControlsSystem = PlayerControlsSystem()
        val sightSystem = SightSystem()
//        val collisionSystem = CollisionSystem()
//        val aiSystem = AISystem()
//        val timeSystem = TimeSystem()
//        val lightSystem = LightSystem()
        val renderLevelSystem = RenderLevelSystem(camera, texture)
        val renderObjectsSystem = RenderObjectsSystem(batch)

        val systems = listOf(
            animationSystem,
            renderLevelSystem,
            renderObjectsSystem,
            levelSystem,
            moveSystem,
            playerControlsSystem,
            sightSystem
        )
        systems.forEach { ecsEngine.addSystem((it)) }

        EventBus.send(Event.NextLevelEvent())
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

    override fun render(delta: Float) {
        ecsEngine.update(min(delta, 1 / 60f))
        super.render(delta)
    }


    override fun rootActor() = window(title = "Game") {

        verticalGroup {
            textButton(text = "Back") {
                onClick { screenManager.goToScreen(MainMenuScreenType) }
            }
        }

        pack()
    }

    override fun screenInputProcessor() = controls
}