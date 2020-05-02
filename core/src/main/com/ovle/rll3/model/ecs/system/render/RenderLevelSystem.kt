package com.ovle.rll3.model.ecs.system.render

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.ovle.rll3.event.Event.EntityMoved
import com.ovle.rll3.event.Event.LevelLoaded
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.advanced.PerceptionComponent
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.ecs.component.util.Mappers.sight
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.model.ecs.system.EventSystem
import com.ovle.rll3.model.procedural.config.LevelParams
import com.ovle.rll3.view.bgColor
import com.ovle.rll3.view.layer.CustomTiledMapTileLayer
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.layer.level.LayerType
import com.ovle.rll3.view.layer.level.TileTextureInfo
import com.ovle.rll3.view.layer.level.TileToTextureParams
import com.ovle.rll3.view.layer.mapLayer
import ktx.ashley.get


class RenderLevelSystem(
    private val camera: OrthographicCamera,
    private val texturesInfo: TexturesInfo
) : EventSystem() {

    private var mapRenderer: TiledMapRenderer? = null
    private var tiledMap: TiledMap? = null


    override fun subscribe() {
        EventBus.subscribe<EntityMoved> { onEntityMoved(it.entity) }
        EventBus.subscribe<LevelLoaded> { onLevelLoaded(it.level, it.levelParams) }
    }

    private fun onLevelLoaded(level: LevelInfo, levelParams: LevelParams) {
        tiledMap = tiledMap(level, levelParams.tileToTexture)
        mapRenderer = OrthogonalTiledMapRenderer(tiledMap)
    }

    private fun tiledMap(tiles: LevelInfo, tileToTexture: (TileToTextureParams) -> TileTextureInfo) =
        TiledMap().apply {
            arrayOf(LayerType.Floor, LayerType.Walls, LayerType.Decoration).forEach {
                layers.add(mapLayer(tiles, texturesInfo, it, tileToTexture))
            }
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

    private fun onEntityMoved(entity: Entity?) {
        if (entity == null) return

        val interactionInfo = playerInteractionInfo() ?: return
        if (interactionInfo.controlledEntity != entity) return

        val sightComponent = entity[sight] ?: return
        markSightArea(sightComponent)
    }

    private fun markSightArea(perceptionComponent: PerceptionComponent) {
        val mapLayers = tiledMap!!.layers
        for (i in 0 until mapLayers.size()) {
            val layer = mapLayers.get(i)
            (layer as CustomTiledMapTileLayer).markVisiblePositions(perceptionComponent.sightPositions)
        }
    }
}