package com.ovle.rll3.model

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.ovle.rll3.model.ecs.component.MoveComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.RenderComponent

class GameEngine {
    private lateinit var ecsEngine: Engine

    fun init(systems: List<EntitySystem>, spriteDrawable: SpriteDrawable) {
        ecsEngine = PooledEngine()
        systems.forEach { ecsEngine.addSystem((it)) }

        val startPosition = Vector2(8f, 8f)
        val playerEntity = ecsEngine.createEntity()
                .add(RenderComponent(spriteDrawable))
                .add(PositionComponent(startPosition))
                .add(MoveComponent(path = listOf(startPosition, Vector2(0f, 14f), Vector2(5f, 1f))))

        ecsEngine.addEntity(playerEntity)
    }

    fun dispose() {
//        ecsEngine.removeAllEntities()
    }

    fun update(delta: Float) {
        ecsEngine.update(delta)
    }
}