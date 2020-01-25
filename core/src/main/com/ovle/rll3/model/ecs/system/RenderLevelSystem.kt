package com.ovle.rll3.model.ecs.system
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.ovle.rll3.Event
import com.ovle.rll3.Event.*
import com.ovle.rll3.EventBus
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.component.SightComponent
import com.ovle.rll3.model.ecs.componentMapper
import com.ovle.rll3.model.ecs.playerInteractionInfo
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.view.*
import com.ovle.rll3.view.layer.CustomTiledMapTileLayer
import com.ovle.rll3.view.layer.LayerType
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.layer.testLayer
import ktx.ashley.get
import kotlin.math.roundToInt


class RenderLevelSystem(
    private val camera: OrthographicCamera,
    private val texturesInfo: TexturesInfo
): EventSystem<Event>() {

    private val sight: ComponentMapper<SightComponent> = componentMapper()

    private var mapRenderer: TiledMapRenderer? = null
    private var tiledMap: TiledMap? = null
    private val selectedScreenPoint = Vector2()


    init {
        RenderConfig.unproject = camera::unproject
    }

    override fun channel() = EventBus.receive<Event>()

    override fun dispatch(event: Event) {
        when (event) {
            is CameraScaleInc -> onScaleChange(0.1f)
            is CameraScaleDec -> onScaleChange(-0.1f)
            is CameraScrolled -> onScaleChange(-event.amount.toFloat() * scaleScrollCoeff)
            is CameraMoved -> onScrollOffsetChange(event.amount)
            is MouseMoved -> onMousePositionChange(event.screenPoint)
            is EntityMoved -> onEntityMoved(event.entity)
            is LevelLoaded -> onLevelLoaded(event.level)
        }
    }

    private fun onLevelLoaded(level: LevelInfo) {
        tiledMap = tiledMap(level)
        mapRenderer = OrthogonalTiledMapRenderer(tiledMap, initialScale)
    }

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

    private fun onMousePositionChange(screenPoint: Vector2) {
        val projected = camera.unproject(Vector3(screenPoint, 0.0f))
        selectedScreenPoint.set(
            ((projected.x.roundToInt() / tileWidth) * tileWidth).toFloat(),
            ((projected.y.roundToInt() / tileHeight) * tileHeight).toFloat()
        )
    }

    private fun onScaleChange(diff: Float) {
        RenderConfig.scale += diff
        camera.zoom -= diff
        camera.update()
    }

    private fun onScrollOffsetChange(diff: Vector2) {
        val scrollOffset = RenderConfig.scrollOffset
        scrollOffset.add(-diff.x, diff.y)
        camera.position.set(scrollOffset.x, scrollOffset.y, 0.0f)
        camera.update()
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