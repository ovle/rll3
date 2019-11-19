
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.*
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.RenderComponent
import com.ovle.rll3.model.ecs.get
import ktx.ashley.get

class RenderSystem(private val batch: Batch) : IteratingSystem(all(RenderComponent::class.java).get()) {

    private val toRender = mutableListOf<Entity>()
    private val screenOffset = Vector2(0f, 0f)

    private val render: ComponentMapper<RenderComponent> = get()
    private val position: ComponentMapper<PositionComponent> = get()

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
        batch.begin()
        entities.forEach {
            val entityRender = it[render]!!
            val position = it[position]!!.position
            val sprite = entityRender.sprite
            val screenPosition = Vector2(position).apply {
                set(screenOffset.x + x * tileWidth * tileMapScale, screenOffset.y + y * tileHeight * tileMapScale)
            }
            val screenSize = Vector2(spriteWidth * spriteScale, spriteHeight * spriteScale)
            sprite.draw(batch, screenPosition.x, screenPosition.y, screenSize.x, screenSize.y)
        }
        batch.end()
    }

}