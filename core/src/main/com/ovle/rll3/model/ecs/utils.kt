package com.ovle.rll3.model.ecs

import com.badlogic.ashley.core.*
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.*
import ktx.ashley.get
import ktx.ashley.has
import kotlin.reflect.KClass


inline fun <reified T : Component> componentMapper(): ComponentMapper<T> = ComponentMapper.getFor(T::class.java)

//todo use families?

fun entitiesWith(entities: Collection<Entity>, componentClass: KClass<out Component>) = ComponentMapper.getFor(componentClass.java)
    .run {
        entities.filter { it.has(this) }
    }

fun entityWithNullable(entities: Collection<Entity>, componentClass: KClass<out Component>) = entitiesWith(entities, componentClass).singleOrNull()
fun entityWith(entities: Collection<Entity>, componentClass: KClass<out Component>) = entityWithNullable(entities, componentClass)!!

inline fun <reified T: Component> Entity.component(componentClass: KClass<out T>) = this[ComponentMapper.getFor(componentClass.java)]

inline fun <reified T: Component> singleComponentNullable(entities: Collection<Entity>, componentClass: KClass<out T>) =
    entityWithNullable(entities.toList(), componentClass)?.component(componentClass) as T?

inline fun <reified T: Component> singleComponent(entities: Collection<Entity>, componentClass: KClass<out T>) =
    singleComponentNullable(entities, componentClass)!!

fun IteratingSystem.hostEntities() = this.entities
fun EntitySystem.allEntities() = this.engine.entities

fun EntitySystem.levelInfoNullable() = singleComponentNullable(allEntities().toList(), LevelComponent::class)?.level
fun EntitySystem.levelInfo() = levelInfoNullable()!!
fun EntitySystem.playerInteractionInfo() = singleComponentNullable(allEntities().toList(), PlayerInteractionComponent::class)

fun Engine.entity(vararg components: Component) = createEntity().apply {
    components.forEach { component ->  this.add(component) }
    addEntity(this)
}

fun Entity.has( componentClass: KClass<out Component>) = this.has(ComponentMapper.getFor(componentClass.java))

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

fun connectionOnPosition(levelInfo: LevelInfo, position: GridPoint2) = entitiesOnPosition(levelInfo, position)
    .singleOrNull {
        it.has(LevelConnectionComponent::class)
    }

fun Collection<Entity>.print() = map { it.print() }.foldRight("entities: ") { acc, it -> "$acc; $it" }

fun Entity.print() = components.map { it.print() }.foldRight("components: ") { acc, it -> "$acc, $it" }

fun Component.print(): String {
    return this.toString()
}