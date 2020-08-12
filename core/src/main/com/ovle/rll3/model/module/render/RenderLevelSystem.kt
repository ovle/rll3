package com.ovle.rll3.model.module.render

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.utils.Array
import com.ovle.rll3.Tile
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.Event.GameEvent.EntityEvent.*
import com.ovle.rll3.event.Event.GameEvent.LevelLoadedEvent
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.perception.PerceptionComponent
import com.ovle.rll3.model.module.game.LevelInfo
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.procedural.config.LevelParams
import com.ovle.rll3.model.tile.*
import com.ovle.rll3.point
import com.ovle.rll3.view.layer.*
import com.ovle.rll3.view.palette.Palette.bgColor
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth


class RenderLevelSystem(
    private val camera: OrthographicCamera,
    assetsManager: AssetsManager
) : EventSystem() {

    private val levelTexturesInfo = assetsManager.levelTexture
    private val textureRegions = TextureRegionsInfo(levelTexturesInfo)

    private var mapRenderer: TiledMapRenderer? = null
    private var tiledMap: TiledMap? = null


    override fun subscribe() {
        EventBus.subscribe<EntityFovUpdatedEvent> { onEntityFovUpdated(it.entity) }
        EventBus.subscribe<LevelLoadedEvent> { onLevelLoaded(it.level, it.levelParams) }
        EventBus.subscribe<DebugTileChanged> { onDebugTileChanged(it.tile, it.position) }
    }

    private fun onLevelLoaded(level: LevelInfo, levelParams: LevelParams) {
        tiledMap = tiledMap(level, ::tileToTexture)
        mapRenderer = OrthogonalTiledMapRenderer(tiledMap)
    }

    private fun tileToTexture(params: TileToTextureParams) =
        TileTextureInfo(
            regions = arrayOf(region(params))
        )

    private fun region(params: TileToTextureParams): TextureRegion {
        val regions = params.textureRegions.regions
        val emptyRegion = regions[7][7]
        return when (params.tile) {
            structureWallSTileId -> regions[3][8]
            structureWallWTileId -> regions[3][9]
            fenceTileId -> regions[3][10]    //todo entity?
            structureFloorSTileId -> regions[3][11]
            structureFloorWTileId -> regions[3][12]

            naturalHighWallTileId -> regions[4][10]
            naturalLowWallTileId -> regions[4][11]

            deepWaterTileId -> regions[4][8]
            shallowWaterTileId -> regions[4][9]

            roadTileId -> regions[3][13]
            highGroundTileId -> regions[3][14]
            lowGroundTileId -> regions[3][15]

            else -> emptyRegion
        }
    }

    private fun tiledMap(tiles: LevelInfo, tileToTexture: (TileToTextureParams) -> TileTextureInfo) =
        TiledMap().apply {
            layers.add(mapLayer(tiles, tileToTexture))
        }


    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        if (mapRenderer != null) {
            draw()
        }
    }

    private fun draw() {
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        mapRenderer!!.setView(camera)
        mapRenderer!!.render()
    }


    fun mapLayer(levelInfo: LevelInfo, tileToTexture: (TileToTextureParams) -> TileTextureInfo): MapLayer {
        val tiles = levelInfo.tiles
        val result = CustomTiledMapTileLayer(tiles.size, tiles.size, tileWidth, tileHeight)
//        val lightTiles = lightTiles(levelInfo)
//        val lightInfo = lightByPosition(lightTiles)

        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
//                val nearTiles = nearValues(tiles, x, y)
                val tile = tiles.get(x, y)
                updateTile(result, tile, point(x, y))
            }
        }

        return result
    }

    private fun textureToCell(tileTextureInfo: TileTextureInfo): TiledMapTileLayer.Cell {
        val cell = TiledMapTileLayer.Cell()
        val regions = tileTextureInfo.regions
        if (regions.isNotEmpty()) {
            val staticTiles = regions.map { StaticTiledMapTile(it) }.toTypedArray()
            cell.tile = if (staticTiles.size == 1) staticTiles.single()
            else AnimatedTiledMapTile(tileTextureInfo.animationInterval, Array(staticTiles))
        }
        return cell
    }

//    fun indexedTextureTile(tileSetConfig: TextureTileSet, index: Int, textureRegions: TextureRegions) =
//        textureRegions[tileSetConfig.originX + index % tileSetConfig.size][tileSetConfig.originY + index / tileSetConfig.size]

    private fun onDebugTileChanged(tile: Tile, position: GridPoint2) {
        val layer = tiledMap!!.layers.single()
        updateTile((layer as TiledMapTileLayer), tile, position)
    }

    private fun updateTile(layer: TiledMapTileLayer, tile: Tile, position: GridPoint2) {
        val params = TileToTextureParams(tile, textureRegions)
        val tileTextureInfo = tileToTexture(params)
        val cell = textureToCell(tileTextureInfo)

        layer.setCell(position.x, position.y, cell)
    }

    private fun onEntityFovUpdated(entity: Entity?) {
//        if (entity == null) return
//
//        val sightComponent = entity[perception] ?: return
//        markSightArea(sightComponent)
    }

    private fun markSightArea(perceptionComponent: PerceptionComponent) {
        val layer = tiledMap!!.layers.single()
        (layer as CustomTiledMapTileLayer).markVisiblePositions(perceptionComponent.fov)
    }
}