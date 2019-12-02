package com.ovle.rll3.model

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.ovle.rll3.model.ecs.component.*
import com.ovle.rll3.model.procedural.grid.LightSourceInfo

class GameEngine {
    private lateinit var ecsEngine: Engine

    fun init(systems: List<EntitySystem>, spriteDrawable: SpriteDrawable, lightsInfo: Set<LightSourceInfo>) {
        ecsEngine = PooledEngine()
        systems.forEach { ecsEngine.addSystem((it)) }

        val gameEntity = ecsEngine.createEntity()
        ecsEngine.addEntity(gameEntity)

        val startPosition = Vector2(8f, 8f)
        val playerEntity = ecsEngine.createEntity()
                .add(PlayerControlledComponent())
                .add(RenderComponent(spriteDrawable))
                .add(PositionComponent(startPosition))
                .add(MoveComponent())

        ecsEngine.addEntity(playerEntity)

        lightsInfo.forEach {
            val lightEntity = ecsEngine.createEntity()
                .add(PositionComponent(Vector2(it.x.toFloat(), it.y.toFloat())))
                .add(LightComponent(5))
            ecsEngine.addEntity(lightEntity)
        }
    }

    fun dispose() {
//        ecsEngine.removeAllEntities()
    }

    fun update(delta: Float) {
        ecsEngine.update(delta)
    }
}