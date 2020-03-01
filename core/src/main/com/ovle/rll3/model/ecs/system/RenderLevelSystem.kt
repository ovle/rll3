package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.ovle.rll3.Event
import com.ovle.rll3.Event.EntityMoved
import com.ovle.rll3.Event.LevelLoaded
import com.ovle.rll3.EventBus
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.component.Mappers.sight
import com.ovle.rll3.model.ecs.component.SightComponent
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.model.procedural.config.LevelSettings
import com.ovle.rll3.view.bgColor
import com.ovle.rll3.view.initialScale
import com.ovle.rll3.view.layer.CustomTiledMapTileLayer
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.layer.level.LayerType
import com.ovle.rll3.view.layer.level.TileToTextureParams
import com.ovle.rll3.view.layer.mapLayer
import ktx.ashley.get


class RenderLevelSystem(
    private val camera: OrthographicCamera,
    private val texturesInfo: TexturesInfo
) : EventSystem<Event>() {

    private var mapRenderer: TiledMapRenderer? = null
    private var tiledMap: TiledMap? = null


    override fun channel() = EventBus.receive<Event>()

    override fun dispatch(event: Event) {
        when (event) {
            is EntityMoved -> onEntityMoved(event.entity)
            is LevelLoaded -> onLevelLoaded(event.level, event.levelSettings)
        }
    }


    private fun onLevelLoaded(level: LevelInfo, levelSettings: LevelSettings) {
        tiledMap = tiledMap(level, levelSettings.tileToTexture)
        mapRenderer = OrthogonalTiledMapRenderer(tiledMap, initialScale)
    }

    private fun tiledMap(tiles: LevelInfo, tileToTexture: (TileToTextureParams) -> Array<TextureRegion>) =
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

    private fun markSightArea(sightComponent: SightComponent) {
        val mapLayers = tiledMap!!.layers
        for (i in 0 until mapLayers.size()) {
            val layer = mapLayers.get(i)
            (layer as CustomTiledMapTileLayer).markVisiblePositions(sightComponent.positions)
        }
    }
}