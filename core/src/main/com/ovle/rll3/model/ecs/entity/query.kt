package com.ovle.rll3.model.ecs.entity

import com.badlogic.ashley.core.*
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.*
import com.ovle.rll3.model.ecs.system.level.ConnectionId
import ktx.ashley.get
import ktx.ashley.has
import kotlin.reflect.KClass


//todo use families?

object EntityQuery {
    fun entitiesWith(entities: Collection<Entity>, componentClass: KClass<out Component>) = ComponentMapper.getFor(componentClass.java)
        .run {
            entities.filter { it.has(this) }
        }

    fun entityWithNullable(entities: Collection<Entity>, componentClass: KClass<out Component>) = entitiesWith(entities, componentClass).singleOrNull()
    fun entityWith(entities: Collection<Entity>, componentClass: KClass<out Component>) = entityWithNullable(entities, componentClass)!!

    fun hasEntityOnPosition(levelInfo: LevelInfo, position: GridPoint2, componentClass: KClass<out Component>): Boolean {
        val positionMapper = componentMapper<PositionComponent>()
        return entitiesWith(levelInfo.objects, componentClass)
            .any {
                it[positionMapper]?.gridPosition?.equals(position) ?: false
            }
    }

    fun entitiesOnPosition(levelInfo: LevelInfo, position: GridPoint2): Collection<Entity> {
        val positionMapper = componentMapper<PositionComponent>()
        return levelInfo.objects.filter {
            it[positionMapper]?.gridPosition?.equals(position) ?: false
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------

    fun connectionOnPosition(levelInfo: LevelInfo, position: GridPoint2) = entitiesOnPosition(levelInfo, position)
        .singleOrNull {
            it.has(LevelConnectionComponent::class)
        }

    fun connection(levelInfo: LevelInfo?, id: ConnectionId?) =
        entitiesWith(levelInfo?.objects
            ?: emptyList(), LevelConnectionComponent::class)
            .find { it.component(LevelConnectionComponent::class)!!.id == id }
}

fun EntitySystem.levelInfoNullable() = ComponentQuery.singleComponentNullable(allEntities().toList(), LevelComponent::class)?.level
fun EntitySystem.levelInfo() = levelInfoNullable()!!
fun EntitySystem.playerInteractionInfo() = ComponentQuery.singleComponentNullable(allEntities().toList(), PlayerInteractionComponent::class)

fun IteratingSystem.hostEntities() = this.entities
fun EntitySystem.allEntities() = this.engine.entities

fun Engine.entity(vararg components: Component) = createEntity().apply {
    components.forEach { component -> this.add(component) }
    addEntity(this)
}
