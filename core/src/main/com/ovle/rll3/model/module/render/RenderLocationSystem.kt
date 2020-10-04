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
import com.ovle.rll3.event.Event.GameEvent.LocationLoadedEvent
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


class RenderLocationSystem(
    private val camera: OrthographicCamera,
    assetsManager: AssetsManager
) : EventSystem() {

    private val textureRegions = TextureRegionsInfo(assetsManager.levelTexture)

    private var mapRenderer: TiledMapRenderer? = null
    private var tiledMap: TiledMap? = null


    override fun subscribe() {
        EventBus.subscribe<LocationLoadedEvent> { onLocationLoaded(it.location) }
        EventBus.subscribe<DebugTileChanged> { onDebugTileChanged(it.tile, it.position) }
        EventBus.subscribe<EntityFovUpdatedEvent> { onEntityFovUpdated(it.entity) }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        if (mapRenderer != null) {
            draw()
        }
    }


    private fun onLocationLoaded(location: LocationInfo) {
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
        val emptyRegion = regions[0][0]
        val isLighted = true //todo
        return when (params.tile) {
            floorTileId -> regions[0][1]
            wallTileId -> regions[0][3]
            pitTileId -> regions[0][0]
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