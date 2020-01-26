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
import com.ovle.rll3.Event
import com.ovle.rll3.Event.*
import com.ovle.rll3.EventBus
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.allEntities
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.component.PlayerInteractionComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.SightComponent
import com.ovle.rll3.model.ecs.componentMapper
import com.ovle.rll3.model.ecs.entityWithNullable
import com.ovle.rll3.model.ecs.playerInteractionInfo
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.view.*
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
    private val position: ComponentMapper<PositionComponent> = componentMapper()
    private val interaction: ComponentMapper<PlayerInteractionComponent> = componentMapper()

    private var mapRenderer: TiledMapRenderer? = null
    private var tiledMap: TiledMap? = null


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

        focusCamera()

        if (mapRenderer != null) {
            draw()
        }
    }

    private fun focusCamera() {
        val interactionEntity = entityWithNullable(allEntities().toList(), PlayerInteractionComponent::class)
            ?: return
        val interactionComponent = interactionEntity[interaction] ?: return
        val focusedEntity = interactionComponent.focusedEntity ?: return
        val focusedPosition = focusedEntity[position]?.position ?: return
        val focusedScreenPosition = floatPoint(
            focusedPosition.x * tileWidth,
            focusedPosition.y * tileHeight
        )

        if (focusedScreenPosition.epsilonEquals(RenderConfig.scrollOffset)) return

        RenderConfig.scrollOffset = focusedScreenPosition
        camera.position.set(RenderConfig.scrollOffset.x, RenderConfig.scrollOffset.y, 0.0f)
        camera.update()
    }


    private fun draw() {
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        mapRenderer!!.setView(camera)
        mapRenderer!!.render()
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