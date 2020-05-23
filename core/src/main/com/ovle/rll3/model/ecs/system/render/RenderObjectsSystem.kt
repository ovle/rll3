package com.ovle.rll3.model.ecs.system.render

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion.split
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.basic.RenderComponent
import com.ovle.rll3.model.ecs.component.special.LevelConnectionComponent
import com.ovle.rll3.model.ecs.component.special.LevelConnectionComponent.*
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.util.Mappers.perception
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.Mappers.render
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.ecs.component.util.has
import com.ovle.rll3.model.ecs.entity.connection
import com.ovle.rll3.model.ecs.entity.controlledEntity
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.point
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.noVisibilityFilter
import com.ovle.rll3.view.sprite.Sprite
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import ktx.ashley.get
import ktx.ashley.has


class RenderObjectsSystem(
    private val batch: Batch,
    spriteTexture: TexturesInfo
) : IteratingSystem(all(RenderComponent::class.java).get()) {

    private var toRender = mutableListOf<Entity>()

    //todo use all texture versions
    private val spriteRegions = split(spriteTexture.texture, spriteWidth.toInt(), spriteHeight.toInt())
    private val defaultSprite = sprite(spriteRegions, 0, 0)


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val renderComponent = entity[render]!!
        initSprites(renderComponent, entity) //todo move somewhere, not to do this for every update for every entity

        if (renderComponent.visible) {
            toRender.add(entity)
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        val controlledEntity = controlledEntity()
        val fov = controlledEntity?.get(perception)?.fov

        toRender.sortWith(compareBy({ it[render]!!.zLevel }, { -it[position]!!.gridPosition.y }))
        if (fov != null && !noVisibilityFilter) {
            //todo too much for each render
            toRender = toRender.filter { isInFov(it, fov) }.toMutableList()
        }

        draw(toRender, deltaTime)
        toRender.clear()
    }

    private fun isInFov(entity: Entity, fov: Set<GridPoint2>): Boolean {
        val positionComponent = entity[position]!!
        return positionComponent.gridPosition in fov
    }

    private fun initSprites(renderComponent: RenderComponent, entity: Entity) {
        val entityTemplate = entity[template]?.template

        if (renderComponent.sprite == null) {
            val spritesConfig = entityTemplate?.sprite ?: entitySprite(entity)
            if (spritesConfig == null) {
                renderComponent.sprite = defaultSprite
                return
            }

            val sprites = spritesConfig.mapValues {
                (_, texturePoint) ->
                sprite(spriteRegions, texturePoint.x, texturePoint.y)
            }

            renderComponent.sprites = sprites
            renderComponent.sprite = sprites["default"]
        }
    }

    private fun entitySprite(entity: Entity): Map<String, GridPoint2>? {
        return when {
            entity.has<LevelConnectionComponent>() -> {
                val type = entity[levelConnection]!!.type
                return mapOf("default" to point(if (type == LevelConnectionType.Up) 14 else 15, 0))
            }
            else -> null
        }
    }


    private fun draw(entities: List<Entity>, deltaTime: Float) {
        batch.begin()

        for (entity in entities) {
            val renderComponent = entity[render]!!
            val position = entity[position]!!.gridPosition
            val region = renderComponent.currentRegion(deltaTime) ?: continue

            batch.draw(floatPoint(position), region, renderComponent.flipped)
        }

        batch.end()
    }
}