package com.ovle.rll3.model.ecs.system
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.ovle.rll3.Event
import com.ovle.rll3.Event.EntityMoved
import com.ovle.rll3.Event.LevelLoaded
import com.ovle.rll3.EventBus
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.component.SightComponent
import com.ovle.rll3.model.ecs.componentMapper
import com.ovle.rll3.model.ecs.playerInteractionInfo
import com.ovle.rll3.view.bgColor
import com.ovle.rll3.view.initialScale
import com.ovle.rll3.view.layer.CustomTiledMapTileLayer
import com.ovle.rll3.view.layer.LayerType
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.layer.testLayer
import ktx.ashley.get


class RenderLevelSystem(
    private val camera: OrthographicCamera,
    private val texturesInfo: TexturesInfo
): EventSystem<Event>() {

    private val sight: ComponentMapper<SightComponent> = componentMapper()

    private var mapRenderer: TiledMapRenderer? = null
    private var tiledMap: TiledMap? = null


    override fun channel() = EventBus.receive<Event>()

    override fun dispatch(event: Event) {
        when (event) {
            is EntityMoved -> onEntityMoved(event.entity)
            is LevelLoaded -> onLevelLoaded(event.level)
        }
    }


    private fun onLevelLoaded(level: LevelInfo) {
        tiledMap = tiledMap(level)
        mapRenderer = OrthogonalTiledMapRenderer(tiledMap, initialScale)
    }

    //todo
    private fun tiledMap(tiles: LevelInfo) = TiledMap().apply {
        layers.add(testLayer(tiles, texturesInfo, LayerType.Floor))
        layers.add(testLayer(tiles, texturesInfo, LayerType.Walls))
        layers.add(testLayer(tiles, texturesInfo, LayerType.Decoration))
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
        if (interactionInfo.controlledEntity != entity)  return

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