package com.ovle.rll3.model.ecs

import com.badlogic.ashley.core.*
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.model.ecs.component.LevelComponent
import ktx.ashley.get
import ktx.ashley.has
import kotlin.reflect.KClass


inline fun <reified T : Component> get(): ComponentMapper<T> = ComponentMapper.getFor(T::class.java)


fun entitiesWith(entities: Collection<Entity>, componentClass: KClass<out Component>) = ComponentMapper.getFor(componentClass.java)
    .run {
        entities.filter { it.has(this) }
    }

fun entityWithNullable(entities: Collection<Entity>, componentClass: KClass<out Component>) = entitiesWith(entities, componentClass).singleOrNull()
fun entityWith(entities: Collection<Entity>, componentClass: KClass<out Component>) = entityWithNullable(entities, componentClass)!!

fun Entity.component(componentClass: KClass<out Component>) = this[ComponentMapper.getFor(componentClass.java)]

inline fun <reified T: Component> singleComponentNullable(entities: Collection<Entity>, componentClass: KClass<out T>) =
    entityWithNullable(entities.toList(), componentClass)?.component(componentClass) as T?

inline fun <reified T: Component> singleComponent(entities: Collection<Entity>, componentClass: KClass<out T>) =
    singleComponentNullable(entities, componentClass)!!

fun IteratingSystem.hostEntities() = this.entities
fun EntitySystem.allEntities() = this.engine.entities


fun EntitySystem.levelInfoNullable() = singleComponentNullable(allEntities().toList(), LevelComponent::class)?.level
fun EntitySystem.levelInfo() = levelInfoNullable()!!


fun Engine.entity(vararg components: Component) = createEntity().apply {
    components.forEach { component ->  this.add(component) }
    addEntity(this)
}