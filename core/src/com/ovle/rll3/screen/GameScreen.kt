package com.ovle.rll3.screen

import RenderSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.ovle.rll3.*
import com.ovle.rll3.ScreenManager.ScreenType.MainMenuScreenType
import com.ovle.rll3.model.GameEngine
import com.ovle.rll3.model.ecs.system.MoveSystem
import com.ovle.rll3.model.procedural.DungeonGridFactory
import com.ovle.rll3.model.procedural.GridToTileArrayMapper
import com.ovle.rll3.model.procedural.createTiles
import com.ovle.rll3.view.tiles.testLayer
import ktx.actors.onClick
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup
import ktx.scene2d.window
import kotlin.math.min


class GameScreen(screenManager: ScreenManager, batch: Batch, assets: AssetManager, camera: OrthographicCamera): BaseScreen(screenManager, batch, assets, camera) {

    lateinit var map: TiledMap
    lateinit var mapRenderer: TiledMapRenderer
    lateinit var texture: Texture
    lateinit var spriteTexture: Texture
    lateinit var sprite: Sprite
    lateinit var spriteDrawable: SpriteDrawable

    lateinit var gameEngine: GameEngine

    override fun show() {
        super.show()

//        assets.setLoader(TiledMap::class.java, TmxMapLoader(InternalFileHandleResolver()))
        assets.setLoader(Texture::class.java, TextureLoader(InternalFileHandleResolver()))

        assets.load(tileTexturePath, Texture::class.java)
        assets.load(spriteTexturePath, Texture::class.java)
        texture = assets.finishLoadingAsset<Texture>(tileTexturePath)
        spriteTexture = assets.finishLoadingAsset<Texture>(spriteTexturePath)

        map = TiledMap()
        val tiles = createTiles(mapSizeInTiles, DungeonGridFactory(), GridToTileArrayMapper())
        map.layers.add(testLayer(tiles, texture))
        mapRenderer = OrthogonalTiledMapRenderer(map, tileMapScale)

        sprite = Sprite(
                spriteTexture,
                (spriteWidth * 0).toInt(), (spriteHeight * 1).toInt(),
                spriteWidth.toInt(), spriteHeight.toInt()
        )
        spriteDrawable = SpriteDrawable(sprite)

        val renderSystem = RenderSystem(batch)
        val moveSystem = MoveSystem()
//        val controlSystem = PlayerControlSystem()
//        val collisionSystem = CollisionSystem()

        gameEngine = GameEngine()
        gameEngine.init(listOf(renderSystem, moveSystem), spriteDrawable)
    }

    override fun render(delta: Float) {
        super.render(delta)

        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val cameraX = 0.0f
        val cameraY = 0.0f
        camera.translate(cameraX, cameraY)
        mapRenderer.setView(camera)
        mapRenderer.render()

        gameEngine.update(min(delta, 1 / 60f))
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
}