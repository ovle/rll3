package com.ovle.rll3.model

import com.badlogic.ashley.core.*
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.ovle.rll3.model.ecs.component.*

class GameEngine {
    private var ecsEngine: Engine = PooledEngine()

    fun init(systems: List<EntitySystem>, spriteDrawable: SpriteDrawable, startPosition: Vector2) {
        systems.forEach { ecsEngine.addSystem((it)) }

        val gameEntity = ecsEngine.createEntity()
        ecsEngine.addEntity(gameEntity)

        val playerEntity = entity(
            PlayerControlledComponent(),
            RenderComponent(spriteDrawable),
            PositionComponent(startPosition),
            MoveComponent(),
            SightComponent(5)
        )

        ecsEngine.addEntity(playerEntity)
    }

    fun entity(vararg components: Component): Entity {
        val result = ecsEngine.createEntity()
        components.forEach { result.add(it) }
        return result
    }

    fun dispose() {
//        ecsEngine.removeAllEntities()
    }

    fun update(delta: Float) {
        ecsEngine.update(delta)
    }
}