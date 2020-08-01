package com.ovle.rll3.model.ecs.system.render

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.Event.LevelLoaded
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.advanced.PerceptionComponent
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.ecs.component.util.Mappers.perception
import com.ovle.rll3.model.ecs.entity.controlledEntity
import com.ovle.rll3.model.ecs.system.EventSystem
import com.ovle.rll3.model.procedural.config.LevelParams
import com.ovle.rll3.model.tile.*
import com.ovle.rll3.view.palette.Palette.bgColor
import com.ovle.rll3.view.layer.CustomTiledMapTileLayer
import com.ovle.rll3.view.layer.TileTextureInfo
import com.ovle.rll3.view.layer.TileToTextureParams
import com.ovle.rll3.view.layer.mapLayer
import ktx.ashley.get


class DebugRenderLevelSystem(
    private val camera: OrthographicCamera,
    assetsManager: AssetsManager
) : EventSystem() {

    private val levelTexturesInfo = assetsManager.levelTexture
    private var mapRenderer: TiledMapRenderer? = null
    private var tiledMap: TiledMap? = null


    override fun subscribe() {
        EventBus.subscribe<Event.EntityFovUpdated> { onEntityFovUpdated(it.entity) }
        EventBus.subscribe<LevelLoaded> { onLevelLoaded(it.level, it.levelParams) }
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
        return when (params.nearTiles.value?.typeId) {
            null -> emptyRegion
            structureWallTileId -> regions[3][1]
            wallTileId -> regions[5][3]
            structureFloorTileId -> regions[3][5]
            groundTileId -> regions[3][6]
            waterTileId -> regions[5][0]
            else -> regions[3][5]
        }
    }

    private fun tiledMap(tiles: LevelInfo, tileToTexture: (TileToTextureParams) -> TileTextureInfo) =
        TiledMap().apply {
            layers.add(mapLayer(tiles, levelTexturesInfo, tileToTexture))
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

    private fun onEntityFovUpdated(entity: Entity?) {
        if (entity == null) return

        if (controlledEntity() != entity) return

        val sightComponent = entity[perception] ?: return
        markSightArea(sightComponent)
    }

    private fun markSightArea(perceptionComponent: PerceptionComponent) {
        val mapLayers = tiledMap!!.layers
        for (i in 0 until mapLayers.size()) {
            val layer = mapLayers.get(i)
            (layer as CustomTiledMapTileLayer).markVisiblePositions(perceptionComponent.fov)
        }
    }
}