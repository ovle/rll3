package com.ovle.rll3.model.module.render

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion.split
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.component.ComponentMappers.render
import com.ovle.rll3.model.module.core.component.ComponentMappers.template
import com.ovle.rll3.point
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import ktx.ashley.get


class RenderObjectsSystem(
    private val batch: Batch,
    assetsManager: AssetsManager
) : IteratingSystem(all(RenderComponent::class.java).get()) {

    private val spriteTexturesInfo = assetsManager.objectsTexture
    private var toRender = mutableListOf<Entity>()

    //todo use all texture versions
    private val spriteRegions = split(spriteTexturesInfo.texture, spriteWidth.toInt(), spriteHeight.toInt())
    private val defaultSprite = sprite(spriteRegions, 6, 0)
    private val defaultEntitySpriteKey = "default"
    private val deadEntitySpriteKey = "dead"
    private val deadEntitySpritePoint = point(7, 0)


    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        subscribe()
    }

    fun subscribe() {
        EventBus.subscribe<EntityDiedEvent> { onEntityDiedEvent(it.entity) }
        EventBus.subscribe<EntityResurrectedEvent> { onEntityResurrectedEvent(it.entity) }
    }

    private fun onEntityDiedEvent(entity: Entity) {
        entity[render]?.switchSprite(deadEntitySpriteKey)
    }

    private fun onEntityResurrectedEvent(entity: Entity) {
        entity[render]?.switchSprite(defaultEntitySpriteKey)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val renderComponent = entity[render]!!
        initSprites(renderComponent, entity) //todo move somewhere, not to do this for every update for every entity

        if (renderComponent.visible) {
            toRender.add(entity)
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        toRender.sortWith(
            compareBy({ it[render]!!.zLevel }, { -it[position]!!.gridPosition.y })
        )

//        val controlledEntity = controlledEntity()
//        val fov = controlledEntity?.get(perception)?.fov
//        if (fov != null && !noVisibilityFilter) {
//            toRender = toRender.filter { isInFov(it, fov) }.toMutableList()
//        }

        draw(toRender)
        toRender.clear()
    }

//    private fun isInFov(entity: Entity, fov: Set<GridPoint2>): Boolean {
//        val positionComponent = entity[position]!!
//        return positionComponent.gridPosition in fov
//    }

    private fun initSprites(renderComponent: RenderComponent, entity: Entity) {
        val viewTemplate = entity[template]?.viewTemplate

        if (renderComponent.sprite == null) {
            val spritesConfig = (entitySprite(entity) ?: viewTemplate?.sprite)?.toMutableMap()
            if (spritesConfig == null) {
                renderComponent.sprite = defaultSprite
                return
            }

            spritesConfig["dead"] = deadEntitySpritePoint

            val sprites = spritesConfig.mapValues { (_, texturePoint) ->
                sprite(spriteRegions, texturePoint.x, texturePoint.y)
            }

            renderComponent.sprites = sprites
            renderComponent.sprite = sprites[defaultEntitySpriteKey]
        }
    }

    /**
     * personal sprite for some kinds of entity
     */
    private fun entitySprite(entity: Entity): Map<String, GridPoint2>? {
        return when {
            else -> null
        }
    }

    private fun draw(entities: List<Entity>) {
        batch.begin()

        for (entity in entities) {
            checkNotNull(entity[render])
            checkNotNull(entity[position])

            val renderComponent = entity[render]!!
            val position = entity[position]!!.gridPosition

            batch.draw(position, renderComponent)
        }

        batch.end()
    }
}