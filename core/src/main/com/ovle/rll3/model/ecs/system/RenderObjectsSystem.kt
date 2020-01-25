package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion.split
import com.ovle.rll3.model.ecs.component.AnimationComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.RenderComponent
import com.ovle.rll3.model.ecs.componentMapper
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.sprite.sprite
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth
import ktx.ashley.get
import kotlin.math.roundToInt

//todo event + iterating ?
class RenderObjectsSystem(
    private val batch: Batch,
    spriteTexture: TexturesInfo
) : IteratingSystem(all(RenderComponent::class.java).get()) {

    private val render: ComponentMapper<RenderComponent> = componentMapper()
    private val animation: ComponentMapper<AnimationComponent> = componentMapper()
    private val position: ComponentMapper<PositionComponent> = componentMapper()

    private val toRender = mutableListOf<Entity>()
    //todo use all texture versions
    private val regions = split(spriteTexture.texture, spriteWidth, spriteHeight)


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val renderComponent = entity[render]!!
        initSprite(renderComponent, entity)

        if (renderComponent.visible) {
            toRender.add(entity)
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        toRender.sortWith(compareBy({ it[render]!!.zLevel }, { -it[position]!!.position.y }))
        draw(toRender, deltaTime)
        toRender.clear()
    }


    private fun initSprite(renderComponent: RenderComponent, entity: Entity) {
        if (renderComponent.sprite == null) {
            renderComponent.sprite = sprite(entity, regions)
        }
    }

    private fun draw(entities: List<Entity>, deltaTime: Float) {
        batch.begin()

        for (entity in entities) {
            val renderComponent = entity[render]!!
            val animationComponent = entity[animation]
            val position = entity[position]!!.position
            val sprite = renderComponent.sprite ?: continue
            val currentAnimation = animationComponent?.current

            val region = currentAnimation?.currentFrame(deltaTime)
                ?: sprite.textureRegion()

//            val screenX = (position.x * tileWidth)
//            val screenY = (position.y * tileHeight)
            val screenX = (position.x.roundToInt() * tileWidth).toFloat()
            val screenY = (position.y.roundToInt() * tileHeight).toFloat()
            batch.draw(
                region,
                screenX, screenY,
                spriteWidth.toFloat(), spriteHeight.toFloat()
            )
        }

        batch.end()
    }
}