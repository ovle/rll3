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
import com.ovle.rll3.Tile
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.event.Event.DebugTileChanged
import com.ovle.rll3.event.Event.GameEvent.EntityFovUpdatedEvent
import com.ovle.rll3.event.Event.GameEvent.LevelLoadedEvent
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.perception.PerceptionComponent
import com.ovle.rll3.model.procedural.config.location.*
import com.ovle.rll3.view.layer.CustomTiledMapTileLayer
import com.ovle.rll3.view.layer.TextureRegionsInfo
import com.ovle.rll3.view.layer.TileToTextureParams
import com.ovle.rll3.view.palette.Palette.bgColor
import com.ovle.rll3.view.tiledMap
import com.ovle.rll3.view.updateTile


class RenderLevelSystem(
    private val camera: OrthographicCamera,
    assetsManager: AssetsManager
) : EventSystem() {

    private val textureRegions = TextureRegionsInfo(assetsManager.levelTexture)

    private var mapRenderer: TiledMapRenderer? = null
    private var tiledMap: TiledMap? = null


    override fun subscribe() {
        EventBus.subscribe<LevelLoadedEvent> { onLevelLoaded(it.location) }
        EventBus.subscribe<DebugTileChanged> { onDebugTileChanged(it.tile, it.position) }
        EventBus.subscribe<EntityFovUpdatedEvent> { onEntityFovUpdated(it.entity) }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        if (mapRenderer != null) {
            draw()
        }
    }


    private fun onLevelLoaded(location: LocationInfo) {
        tiledMap = tiledMap(location.tiles, textureRegions, ::tileToTextureRegion)
        mapRenderer = OrthogonalTiledMapRenderer(tiledMap)
    }

    private fun onDebugTileChanged(tile: Tile, position: GridPoint2) {
        val layer = tiledMap!!.layers.single() as TiledMapTileLayer
        layer.updateTile(tile, position, textureRegions, ::tileToTextureRegion)
    }

    private fun onEntityFovUpdated(entity: Entity?) {
//        if (entity == null) return
//
//        val sightComponent = entity[perception] ?: return
//        markSightArea(sightComponent)
    }


    private fun tileToTextureRegion(params: TileToTextureParams): TextureRegion {
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

            desertTileId -> regions[6][8]
            tundraTileId -> regions[6][10]
            jungleTileId -> regions[6][12]

            else -> emptyRegion
        }
    }

    private fun draw() {
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