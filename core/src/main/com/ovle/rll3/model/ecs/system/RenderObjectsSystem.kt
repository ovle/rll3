package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion.split
import com.ovle.rll3.model.ecs.component.Mappers.animation
import com.ovle.rll3.model.ecs.component.Mappers.position
import com.ovle.rll3.model.ecs.component.Mappers.render
import com.ovle.rll3.model.ecs.component.RenderComponent
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.roundToClosestByAbsInt
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.sprite.sprite
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import com.ovle.rll3.view.tileWidth
import ktx.ashley.get


class RenderObjectsSystem(
    private val batch: Batch,
    spriteTexture: TexturesInfo
) : IteratingSystem(all(RenderComponent::class.java).get()) {

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
        draw(toRender, deltaTime, RenderConfig)
        toRender.clear()
    }

    private fun initSprite(renderComponent: RenderComponent, entity: Entity) {
        if (renderComponent.sprite == null) {
            renderComponent.sprite = sprite(entity, regions)
        }
    }


    private fun draw(entities: List<Entity>, deltaTime: Float, renderConfig: RenderConfig) {
        batch.begin()

        for (entity in entities) {
            val renderComponent = entity[render]!!
            val animationComponent = entity[animation]
            val position = entity[position]!!.position
            val sprite = renderComponent.sprite ?: continue
            val currentAnimation = animationComponent?.currentAnimation
            val region = currentAnimation?.currentFrame(deltaTime)
                ?: sprite.textureRegion()

            val scale = renderConfig.scale
            val screenX = (position.x * scale).roundToClosestByAbsInt() * tileWidth
            val screenY = (position.y * scale).roundToClosestByAbsInt() * tileWidth
            batch.draw(
                region,
                screenX.toFloat(),
                screenY.toFloat(),
                spriteWidth.toFloat() * scale,
                spriteHeight.toFloat() * scale
            )
        }

        batch.end()
    }
}