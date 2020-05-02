package com.ovle.rll3.model.ecs.system.render

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion.split
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.basic.RenderComponent
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.component.util.Mappers.animation
import com.ovle.rll3.model.ecs.component.util.Mappers.playerInteraction
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.Mappers.render
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.ecs.entity.playerInteraction
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.roundToClosestByAbsInt
import com.ovle.rll3.view.layer.TextureRegions
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.sprite.Sprite
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
    private val spriteRegions = split(spriteTexture.texture, spriteWidth, spriteHeight)
    private val defaultSprite = sprite(spriteRegions, 0, 0)


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val renderComponent = entity[render]!!
        initSprite(renderComponent, entity) //todo move somewhere, not to do this for every update for every entity

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
        val entityTemplate = entity[template]?.template

        if (renderComponent.sprite == null) {
            renderComponent.sprite = if (entityTemplate == null) defaultSprite
                else sprite(entityTemplate, spriteRegions)
        }
    }

    private fun sprite(entityTemplate: EntityTemplate?, regions: TextureRegions): Sprite =
        entityTemplate?.sprite?.run {
            val result= this.random()   //todo random ?
            sprite(regions, result.x, result.y)
        } ?: defaultSprite


    private fun draw(entities: List<Entity>, deltaTime: Float) {
        batch.begin()

        for (entity in entities) {
            val renderComponent = entity[render]!!
            val sprite = renderComponent.sprite ?: continue
            val animationComponent = entity[animation]
            val position = entity[position]!!.position
            val currentAnimation = animationComponent?.currentAnimation
            val region = currentAnimation?.currentFrame(deltaTime)
                ?: sprite.textureRegion()

            batch.draw(position, region)
        }

        batch.end()
    }
}