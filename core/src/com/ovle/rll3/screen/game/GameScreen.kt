package com.ovle.rll3.screen.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.ScreenManager.ScreenType.MainMenuScreenType
import com.ovle.rll3.model.GameEngine
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.system.*
import com.ovle.rll3.model.procedural.grid.DungeonGridFactory
import com.ovle.rll3.model.procedural.grid.GridToTileArrayMapper
import com.ovle.rll3.model.procedural.grid.tiles
import com.ovle.rll3.model.procedural.mapSizeInTiles
import com.ovle.rll3.model.tile.TilePassType
import com.ovle.rll3.model.tile.entityTilePassMapper
import com.ovle.rll3.model.tile.vectorCoords
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.view.sprite.sprite
import com.ovle.rll3.view.spriteTexturePath
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
    lateinit var playerSprite: SpriteDrawable

    lateinit var gameEngine: GameEngine
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

        gameEngine = GameEngine()
        val mapSize = mapSizeInTiles
        val tiles = tiles(mapSize, DungeonGridFactory(), GridToTileArrayMapper(), gameEngine)
        val (map) = tiledMap(tiles)
        this.map = map

        //todo
        playerSprite = sprite(spriteTexture, 0, 2)

        val renderSystem = RenderSystem(map, batch, camera, spriteTexture)
        val animationSystem = AnimationSystem()
        val moveSystem = MoveSystem()
        val playerControlsSystem = PlayerControlsSystem()
        val sightSystem = SightSystem()
//        val collisionSystem = CollisionSystem()
//        val aiSystem = AISystem()
//        val timeSystem = TimeSystem()
//        val lightSystem = LightSystem()

        val systems = listOf(animationSystem, renderSystem, moveSystem, playerControlsSystem, sightSystem)
        val startTile = tiles.tiles.filterNotNull().find { entityTilePassMapper(it) == TilePassType.Passable }
        //todo bad coupling
        gameEngine.init(systems, playerSprite, vectorCoords(startTile!!), tiles)
    }

    private fun tiledMap(tiles: LevelInfo): Pair<TiledMap, LevelInfo> {
        val map = TiledMap()
        map.layers.add(testLayer(tiles, texture, LayerType.Floor))
        map.layers.add(testLayer(tiles, texture, LayerType.Walls))
        map.layers.add(testLayer(tiles, texture, LayerType.Decoration))
        return Pair(map, tiles)
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