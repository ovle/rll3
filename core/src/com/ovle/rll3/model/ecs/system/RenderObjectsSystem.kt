package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.RenderComponent
import com.ovle.rll3.model.ecs.componentMapper
import com.ovle.rll3.view.sprite.sprite
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth
import com.ovle.rll3.view.layer.TexturesInfo
import ktx.ashley.get


class RenderObjectsSystem(
    private val batch: Batch,
    private val spriteTexture: TexturesInfo
) : IteratingSystem(all(RenderComponent::class.java).get()) {

    private val render: ComponentMapper<RenderComponent> = componentMapper()
    private val position: ComponentMapper<PositionComponent> = componentMapper()

    private val toRender = mutableListOf<Entity>()


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val renderComponent = entity[render]!!
        if (renderComponent.sprite == null) {
            //todo use all texture versions
            val texture = spriteTexture.texture
            renderComponent.sprite = sprite(entity, texture)
        }

        if (renderComponent.visible) {
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
        for (entity in entities) {
            val entityRender = entity[render]!!
            val position = entity[position]!!.position
            val sprite = entityRender.sprite ?: continue

            sprite.draw(batch, position.x * tileWidth, position.y * tileHeight, spriteWidth, spriteHeight)
        }
//        selectedTileSprite.draw(batch, selectedScreenPoint.x, selectedScreenPoint.y, tileWidth.toFloat(), tileHeight.toFloat())
        batch.end()
    }
}