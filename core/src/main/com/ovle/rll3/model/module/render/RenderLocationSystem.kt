package com.ovle.rll3.model.module.render

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.Tile
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rlUtil.gdx.view.PaletteManager
import com.ovle.rlUtil.gdx.view.textureRegions
import com.ovle.rlUtil.gdx.view.tileMap.CustomTiledMapTileLayer
import com.ovle.rlUtil.gdx.view.tileMap.TileToTextureParams
import com.ovle.rlUtil.gdx.view.tileMap.cell
import com.ovle.rlUtil.gdx.view.tileMap.tiledMap
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.event.EntityFovUpdatedEvent
import com.ovle.rll3.event.LocationLoadedEvent
import com.ovle.rll3.event.TileChangedEvent
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.game.dto.LocationInfo
import com.ovle.rll3.model.module.perception.PerceptionComponent
import com.ovle.rll3.model.procedural.config.location.*
import com.ovle.rll3.view.textureTileSize
import com.ovle.rll3.view.tileSize


class RenderLocationSystem(
    private val camera: OrthographicCamera,
    assetsManager: AssetsManager,
    private val paletteManager: PaletteManager
) : EventSystem() {

    private val textureRegions = textureRegions(assetsManager.levelTexture, paletteManager, textureTileSize)

    private var mapRenderer: TiledMapRenderer? = null
    private var tiledMap: TiledMap? = null


    override fun subscribe() {
        subscribe<LocationLoadedEvent> { onLocationLoaded(it.location) }
        subscribe<TileChangedEvent> { onTileChangedEvent(it.tile, it.position) }
        subscribe<EntityFovUpdatedEvent> { onEntityFovUpdated(it.entity) }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        if (mapRenderer != null) {
            draw()
        }
    }


    private fun onLocationLoaded(location: LocationInfo) {
        tiledMap = tiledMap(location.tiles, textureRegions, ::tileToTextureRegion, tileSize)
        mapRenderer = OrthogonalTiledMapRenderer(tiledMap)
    }

    private fun onTileChangedEvent(tile: Tile, position: GridPoint2) {
        val layer = tiledMap!!.layers.single() as TiledMapTileLayer

        val cell = cell(tile, textureRegions, ::tileToTextureRegion)
        layer.setCell(position.x, position.y, cell)
    }

    private fun onEntityFovUpdated(entity: Entity?) {
//        if (entity == null) return
//
//        val sightComponent = entity[perception] ?: return
//        markSightArea(sightComponent)
    }


    private fun tileToTextureRegion(params: TileToTextureParams): TextureRegion {
        val regions = params.textureRegions
        val emptyRegion = regions[0][7]

        return when (params.tile) {
            structureWallSTileId -> regions[4][0]
            structureWallWTileId -> regions[4][1]
            fenceTileId -> regions[4][2]    //todo entity
            structureFloorSTileId -> regions[4][3]
            structureFloorWTileId -> regions[4][4]
            roadTileId -> regions[4][5]

            deepWaterTileId -> regions[2][0]
            shallowWaterTileId -> regions[2][1]
            naturalHighWallTileId -> regions[2][2]
            naturalLowWallTileId -> regions[2][3]

            highGroundTileId -> regions[3][0]
            lowGroundTileId -> regions[3][0]
            aridTileId -> regions[3][1]
            desertTileId -> regions[3][2]
            jungleTileId -> regions[3][3]
            tundraTileId -> regions[3][4]

            else -> emptyRegion
        }
    }

    private fun draw() {
        val bgColor = paletteManager.bgColor
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        mapRenderer!!.setView(camera)
        mapRenderer!!.render()
    }

    private fun markSightArea(perceptionComponent: PerceptionComponent) {
        val layer = tiledMap!!.layers.single()
        (layer as CustomTiledMapTileLayer).markVisiblePositions(perceptionComponent.fov)
    }
}