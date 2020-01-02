package com.ovle.rll3.model.ecs.system
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.ovle.rll3.Event
import com.ovle.rll3.Event.*
import com.ovle.rll3.EventBus
import com.ovle.rll3.model.ecs.component.PlayerControlledComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.RenderComponent
import com.ovle.rll3.model.ecs.component.SightComponent
import com.ovle.rll3.model.ecs.get
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.view.*
import com.ovle.rll3.view.sprite.sprite
import com.ovle.rll3.view.tiles.CustomTiledMapTileLayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import ktx.ashley.get


class RenderSystem(
    private var map: TiledMap,  //todo ?
    private val batch: Batch,
    private val camera: OrthographicCamera,
    private val spriteTexture: Texture
)
    : IteratingSystem(all(RenderComponent::class.java).get()), CoroutineScope by GlobalScope {

    private val toRender = mutableListOf<Entity>()

    private var mapRenderer: TiledMapRenderer = OrthogonalTiledMapRenderer(map, initialScale)

    private val render: ComponentMapper<RenderComponent> = get()
    private val position: ComponentMapper<PositionComponent> = get()
    private val sight: ComponentMapper<SightComponent> = get()
    private val playerControlled: ComponentMapper<PlayerControlledComponent> = get()

    private lateinit var channel: ReceiveChannel<Event>

    private val selectedScreenPoint = Vector2()
    private var selectedTileSprite: SpriteDrawable


    init {
        RenderConfig.unproject = camera::unproject
        selectedTileSprite = sprite(spriteTexture, 0, 0)
    }

    //    todo move these to separate class
    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)

        launch {
            channel = EventBus.receive()
            for (event in channel) {
                dispatch(event)
            }
        }

        //todo
//        val family = all(PlayerControlledComponent::class.java)
//        val playerEntity = engine!!.getEntitiesFor(family.get()).singleOrNull()
//        onEntityMoved(playerEntity)
    }

    //    todo move these to separate class
    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        channel.cancel()
    }

    
    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (entity[render]!!.visible) {
            toRender.add(entity)
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        toRender.sortWith(compareBy({ it[render]!!.zLevel }, { -it[position]!!.position.y }))
        draw(toRender)
        toRender.clear()
    }

    private fun draw(entities: List<Entity>) {
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        mapRenderer.setView(camera)
        mapRenderer.render()

        drawEntities(entities)
//        drawGUI()
    }

    private fun drawEntities(entities: List<Entity>) {
        batch.begin()
        entities.forEach {
            val entityRender = it[render]!!
            val position = it[position]!!.position
            val sprite = entityRender.sprite

            sprite.draw(batch, position.x * tileWidth, position.y * tileHeight, spriteWidth, spriteHeight)
        }
        selectedTileSprite.draw(batch, selectedScreenPoint.x, selectedScreenPoint.y, tileWidth.toFloat(), tileHeight.toFloat())
        batch.end()
    }

//todo
//    private fun drawGUI() {
//    }

    private fun dispatch(event: Event) {
        when (event) {
            is CameraScaleInc -> onScaleChange(0.1f)
            is CameraScaleDec -> onScaleChange(-0.1f)
            is CameraScrolled -> onScaleChange(-event.amount.toFloat() * scaleScrollCoeff)
            is CameraMoved -> onScrollOffsetChange(event.amount)
            is MouseMoved -> onMousePositionChange(event.screenPoint)
            is EntityMoved -> onEntityMoved(event.entity)
        }
    }

    private fun onMousePositionChange(screenPoint: Vector2) {
        val projected = camera.unproject(Vector3(screenPoint, 0.0f))
        selectedScreenPoint.set(
            ((projected.x.toInt() / tileWidth) * tileWidth).toFloat(),
            ((projected.y.toInt() / tileHeight) * tileHeight).toFloat()
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

        val playerControlledComponent = entity[playerControlled] ?: return
        val sightComponent = entity[sight] ?: return

        markSightArea(sightComponent)
    }

    private fun markSightArea(sightComponent: SightComponent) {
        val mapLayers = map.layers
        for (i in 0 until mapLayers.size()) {
            val layer = mapLayers.get(i)
            (layer as CustomTiledMapTileLayer).markVisiblePositions(sightComponent.positions)
        }
    }

//    private fun onMapChange(newMap: TiledMap) {
//        map = newMap
//        mapRenderer = OrthogonalTiledMapRenderer(map, scale)
//    }
}