
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.Event.*
import com.ovle.rll3.EventBus
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.RenderComponent
import com.ovle.rll3.model.ecs.get
import com.ovle.rll3.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import ktx.ashley.get


class RenderSystem(private val batch: Batch, private val camera: OrthographicCamera, private var map: TiledMap)
    : IteratingSystem(all(RenderComponent::class.java).get()), CoroutineScope by GlobalScope {

    private val toRender = mutableListOf<Entity>()

    private var mapRenderer: TiledMapRenderer = OrthogonalTiledMapRenderer(map, initialScale)

    private val render: ComponentMapper<RenderComponent> = get()
    private val position: ComponentMapper<PositionComponent> = get()
    private    //todo move system should know that ?

    lateinit var channel: ReceiveChannel<PlayerControlEvent>

    //    todo move these to separate class
    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)

        launch {
            channel = EventBus.receive()
            for (event in channel) {
                println(event)
                dispatch(event)
            }
        }
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
//        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        mapRenderer.setView(camera)
        mapRenderer.render()

        drawEntities(entities)
    }

    private fun drawEntities(entities: List<Entity>) {
        batch.begin()
        entities.forEach {
            val entityRender = it[render]!!
            val position = it[position]!!.position
            val sprite = entityRender.sprite

            sprite.draw(batch, position.x * tileWidth, position.y * tileHeight, spriteWidth, spriteHeight)
        }
        batch.end()
    }

    private fun dispatch(event: PlayerControlEvent) {
        when (event) {
            is CameraScaleInc -> onScaleChange(0.1f)
            is CameraScaleDec -> onScaleChange(-0.1f)
            is CameraScrolled -> onScaleChange(-event.amount.toFloat() * scaleScrollCoeff)
            is CameraMoved -> onScrollOffsetChange(event.amount)
        }
    }

    private fun onScaleChange(diff: Float) {
        renderConfig.scale += diff
        camera.zoom -= diff
        camera.update()
    }

    private fun onScrollOffsetChange(diff: Vector2) {
        val scrollOffset = renderConfig.scrollOffset
        scrollOffset.add(-diff.x, diff.y)
        camera.position.set(scrollOffset.x, scrollOffset.y, 0.0f)
    }

//    private fun onMapChange(newMap: TiledMap) {
//        map = newMap
//        mapRenderer = OrthogonalTiledMapRenderer(map, scale)
//    }
}