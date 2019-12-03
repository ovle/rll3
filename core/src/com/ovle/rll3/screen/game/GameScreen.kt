package com.ovle.rll3.screen.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.ScreenManager.ScreenType.MainMenuScreenType
import com.ovle.rll3.model.GameEngine
import com.ovle.rll3.model.ecs.system.AnimationSystem
import com.ovle.rll3.model.ecs.system.MoveSystem
import com.ovle.rll3.model.ecs.system.PlayerControlsSystem
import com.ovle.rll3.model.ecs.system.RenderSystem
import com.ovle.rll3.model.procedural.grid.DungeonGridFactory
import com.ovle.rll3.model.procedural.grid.GridToTileArrayMapper
import com.ovle.rll3.model.procedural.grid.createTiles
import com.ovle.rll3.model.procedural.mapSizeInTiles
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteTexturePath
import com.ovle.rll3.view.spriteWidth
import com.ovle.rll3.view.tileTexturePath
import com.ovle.rll3.view.tiles.LayerType
import com.ovle.rll3.view.tiles.testLayer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ktx.actors.onClick
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup
import ktx.scene2d.window
import kotlin.math.min


@ExperimentalCoroutinesApi
class GameScreen(screenManager: ScreenManager, batch: Batch, assets: AssetManager, camera: OrthographicCamera): BaseScreen(screenManager, batch, assets, camera) {

    lateinit var map: TiledMap

    lateinit var texture: Texture
    lateinit var spriteTexture: Texture
    lateinit var sprite: Sprite
    lateinit var spriteDrawable: SpriteDrawable

    lateinit var gameEngine: GameEngine
    private val controls = PlayerControls()

    override fun show() {
        super.show()

        //todo async loading on separate screen
//        assets.setLoader(TiledMap::class.java, TmxMapLoader(InternalFileHandleResolver()))
        assets.setLoader(Texture::class.java, TextureLoader(InternalFileHandleResolver()))

        assets.load(tileTexturePath, Texture::class.java)
        assets.load(spriteTexturePath, Texture::class.java)
        texture = assets.finishLoadingAsset<Texture>(tileTexturePath)
        spriteTexture = assets.finishLoadingAsset<Texture>(spriteTexturePath)

        map = TiledMap()
        val tiles = createTiles(mapSizeInTiles, DungeonGridFactory(), GridToTileArrayMapper())
        val lightsInfo = tiles.lights()
        map.layers.add(testLayer(tiles, texture, LayerType.Floor))
        map.layers.add(testLayer(tiles, texture, LayerType.Walls))
        map.layers.add(testLayer(tiles, texture, LayerType.Decoration))

        spriteDrawable = playerSprite()

        val renderSystem = RenderSystem(map, batch, camera)
        val animationSystem = AnimationSystem()
        val moveSystem = MoveSystem()
        val playerControlsSystem = PlayerControlsSystem()
//        val collisionSystem = CollisionSystem()
//        val aiSystem = AISystem()
//        val timeSystem = TimeSystem()
//        val lightSystem = LightSystem()

        val systems = listOf(animationSystem, renderSystem, moveSystem, playerControlsSystem)
        gameEngine = GameEngine()
        //todo bad coupling
        gameEngine.init(systems, spriteDrawable, lightsInfo)
    }

    private fun playerSprite(): SpriteDrawable {
        sprite = Sprite(
                spriteTexture,
                (spriteWidth * 0).toInt(), (spriteHeight * 1).toInt(),
                spriteWidth.toInt(), spriteHeight.toInt()
        )
        return SpriteDrawable(sprite)
    }

    override fun hide() {
        super.hide()

        //todo free resources?
    }

    override fun render(delta: Float) {
        gameEngine.update(min(delta, 1 / 60f))
        super.render(delta)
    }

    override fun dispose() {
        map.dispose()

        super.dispose()
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