package com.ovle.rll3.screen

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.ovle.rll3.*
import com.ovle.rll3.ScreenManager.ScreenType.GameScreenType
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.event.Event.PlayerControlEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.subscribe
import com.ovle.rll3.model.module.render.draw
import com.ovle.rll3.model.module.render.sprite
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.config.world.*
import com.ovle.rll3.model.procedural.grid.world.WorldFactory
import com.ovle.rll3.model.procedural.grid.world.WorldInfo
import com.ovle.rll3.screen.game.InitGameInfo
import com.ovle.rll3.view.baseSize
import com.ovle.rll3.view.cameraMoveCoeff
import com.ovle.rll3.view.layer.TextureRegionsInfo
import com.ovle.rll3.view.layer.TileToTextureParams
import com.ovle.rll3.view.scaleScrollCoeff
import com.ovle.rll3.view.tiledMap
import ktx.actors.onClick
import ktx.scene2d.label
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup
import kotlin.random.Random


class WorldScreen(
    private val assetsManager: AssetsManager,
    screenManager: ScreenManager, batch: Batch, camera: OrthographicCamera
) : BaseScreen(screenManager, batch, camera) {

    private val worldFactory = WorldFactory(worldParams)
    private lateinit var world: WorldInfo
    private var seed: Seed = 123L

    private val textureRegions by lazy { TextureRegionsInfo(assetsManager.levelTexture) }
    private val cursorSprite by lazy { sprite(textureRegions.regions, 3, 0) }
    private val selectionSprite by lazy { sprite(textureRegions.regions, 6, 0) }

    private var mapRenderer: TiledMapRenderer? = null
    private var tiledMap: TiledMap? = null

    private val controls = PlayerControls(batchViewport)
    private var cursorPoint: GridPoint2? = null
    private var locationPoint: GridPoint2? = null


    override fun rootActor() =
        verticalGroup {
            label(text = "The world") {}

            textButton(text = "Generate") {
                onClick { onGenerateWorldClick() }
            }
            textButton(text = "Embark!") {
                onClick { onEmbarkClick() }
            }

            setFillParent(true)
            align(Align.bottom)
            pack()
        }

    override fun show() {
        super.show()

        //129
//        camera.zoom = 2.5f
//        camera.position.set(500.0f, 500.0f, 0.0f)

        //257
        camera.zoom = 5.0f
        camera.position.set(1000.0f, 900.0f, 0.0f)

        subscribe<CameraScrollCommand> { onScaleChange(-it.amount.toFloat() * scaleScrollCoeff) }
        subscribe<DragEvent> { onCameraMoved(it.lastDiff) }
        subscribe<MouseMovedEvent> { onMouseMoved(it.viewportPoint) }
        subscribe<MouseClickEvent> { onMouseClick(it.viewportPoint) }

        generateWorld()
    }

    override fun hide() {
        super.hide()

        EventBus.clearSubscriptions()
    }

    override fun screenInputProcessor() = controls

    override fun renderIntr(delta: Float) {
        mapRenderer?.let {
            it.setView(camera)
            it.render()
        }

        renderInteractionInfo()
    }

    private fun renderInteractionInfo() {
        batch.begin()

        cursorPoint?.let {
            it.nearD().forEach {
                p -> batch.draw(vec2(p), cursorSprite.textureRegion())
            }
        }

        locationPoint?.let {
            it.nearHV().forEach {
                p -> batch.draw(vec2(p), selectionSprite.textureRegion())
            }
        }

        batch.end()
    }

    private fun onGenerateWorldClick() {
        generateWorld()
    }

    private fun onEmbarkClick() {
        screenManager.goToScreen(
            GameScreenType,
            InitGameInfo(world, locationPoint!!)
        )
    }

    private fun onScaleChange(diff: Float) {
        camera.zoom -= diff
        println(camera.zoom)
        camera.update()
    }

    private fun onCameraMoved(amount: Vector2) {
        camera.position.add(amount.x * cameraMoveCoeff, amount.y * cameraMoveCoeff, 0.0f)
        println(camera.position)
        camera.update()
    }

    private fun onMouseMoved(viewportPoint: Vector2) {
        cursorPoint = point(viewportPoint.x / baseSize, viewportPoint.y / baseSize)
    }

    private fun onMouseClick(viewportPoint: Vector2) {
        val point = point(viewportPoint.x / baseSize, viewportPoint.y / baseSize)
        if (world.tiles.isPointValid(point)) {
            locationPoint = point
        }
    }

    private fun generateWorld() {
        world = worldFactory.get(RandomParams(seed))
        seed = Random.nextLong()

        tiledMap = tiledMap(world.tiles, textureRegions, ::tileToTextureRegion)
        mapRenderer = OrthogonalTiledMapRenderer(tiledMap)
    }

    private fun tileToTextureRegion(params: TileToTextureParams): TextureRegion {
        val regions = params.textureRegions.regions
        val emptyRegion = regions[0][7]
        return when (params.tile) {
            highMountainTileId -> regions[0][0]
            lowDesertMountainTileId -> regions[0][1]
            lowMountainTileId -> regions[0][2]

            shallowWaterTileId -> regions[0][6]
            deepWaterTileId -> regions[0][7]

            desertTileId -> regions[0][3]
            temperateTileId -> regions[0][4]
            borealTileId -> regions[0][5]

            else -> emptyRegion
        }
    }
}