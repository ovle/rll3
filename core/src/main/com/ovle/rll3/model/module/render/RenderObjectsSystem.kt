package com.ovle.rll3.model.module.render

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion.split
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rlUtil.gdx.math.point
import com.ovle.rlUtil.gdx.view.sprite
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.event.EntityDiedEvent
import com.ovle.rll3.event.EntityResurrectedEvent
import com.ovle.rll3.model.module.core.Components.template
import com.ovle.rll3.model.module.core.entity.position
import com.ovle.rll3.model.module.core.system.BaseIteratingSystem
import com.ovle.rll3.model.module.render.Components.render
import com.ovle.rll3.model.module.space.Components.position
import com.ovle.rll3.view.spriteSize
import ktx.ashley.get


class RenderObjectsSystem(
    private val batch: Batch,
    assetsManager: AssetsManager
) : BaseIteratingSystem(all(RenderComponent::class.java).get()) {

    private val spriteTexturesInfo = assetsManager.objectsTexture
    private var toRender = mutableListOf<Entity>()

    private val spriteRegions = split(spriteTexturesInfo, spriteSize.toInt(), spriteSize.toInt())
    private val defaultSprite = sprite(spriteRegions, 6, 0)
    private val defaultEntitySpriteKey = "default"
    private val deadEntitySpriteKey = "dead"
    private val deadEntitySpritePoint = point(7, 0)


    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        subscribe()
    }

    fun subscribe() {
        subscribe<EntityDiedEvent> { onEntityDiedEvent(it.entity) }
        subscribe<EntityResurrectedEvent> { onEntityResurrectedEvent(it.entity) }
    }

    private fun onEntityDiedEvent(entity: Entity) {
        entity[render]?.switchSprite(deadEntitySpriteKey)
    }

    private fun onEntityResurrectedEvent(entity: Entity) {
        entity[render]?.switchSprite(defaultEntitySpriteKey)
    }

    override fun processEntityIntr(entity: Entity, deltaTime: Float) {
        val renderComponent = entity[render]!!
        initSprites(renderComponent, entity) //todo move somewhere, not to do this for every update for every entity

        if (renderComponent.visible) {
            toRender.add(entity)
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        toRender.sortWith(
            compareBy({ it[render]!!.zLevel }, { -it.position().y })
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
            val position = entity.position()

            batch.draw(position, renderComponent)
        }

        batch.end()
    }
}