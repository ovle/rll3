import com.badlogic.ashley.core.ComponentMapper
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
import com.ovle.rll3.*
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.RenderComponent
import com.ovle.rll3.model.ecs.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ktx.ashley.get

data class RenderConfig(
        var scale: Float = initialScale,
        var scrollOffset: Vector2 = Vector2()
)

class RenderSystem(private val batch: Batch, private val camera: OrthographicCamera, private var map: TiledMap)
    : IteratingSystem(all(RenderComponent::class.java).get()), CoroutineScope by GlobalScope {

    private val toRender = mutableListOf<Entity>()

    private var mapRenderer: TiledMapRenderer = OrthogonalTiledMapRenderer(map, initialScale)

    private val render: ComponentMapper<RenderComponent> = get()
    private val position: ComponentMapper<PositionComponent> = get()
    private val renderConfig = RenderConfig()

    init {
        launch {
            val channel = EventBus.receive<Event.PlayerControlEvent>()
            for (event in channel) {
                println(event)
                dispatch(event)
            }
        }
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

        val scrollOffset = renderConfig.scrollOffset
        camera.position.set(scrollOffset, 0.0f)
        mapRenderer.setView(camera)
        mapRenderer.render()

        drawEntities(entities)
    }

    private fun drawEntities(entities: List<Entity>) {
        val (scale, scrollOffset) = renderConfig

        batch.begin()
        entities.forEach {
            val entityRender = it[render]!!
            val position = it[position]!!.position
            val sprite = entityRender.sprite
            //todo
            if (sprite.sprite.scaleX != scale) {
                sprite.sprite.scale(scale)
            }

            val screenPosition = Vector2(position).apply {
                set(scrollOffset.x + x * tileWidth * scale, y * tileHeight * scale - scrollOffset.y)
            }
            val screenSize = Vector2(spriteWidth * scale, spriteHeight * scale)
            sprite.draw(batch, screenPosition.x, screenPosition.y, screenSize.x, screenSize.y)
        }
        batch.end()
    }


    private fun dispatch(event: Event.PlayerControlEvent) {
        when (event) {
            is Event.CameraScaleInc -> {
                renderConfig.scale += 0.1f
            }
            is Event.CameraScaleDec -> {
                renderConfig.scale -= 0.1f
            }
            is Event.CameraScrolled -> {
                renderConfig.scale -= event.amount
            }
            is Event.CameraMoved -> {
                renderConfig.scrollOffset.add(-event.amount.x, event.amount.y)
            }
        }
    }

////    todo
//    private fun onScaleChange() {
//        mapRenderer = OrthogonalTiledMapRenderer(map, scale)
//    }
//
//    private fun onMapChange(newMap: TiledMap) {
//        map = newMap
//        mapRenderer = OrthogonalTiledMapRenderer(map, scale)
//    }
}