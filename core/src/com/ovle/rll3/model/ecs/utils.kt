package com.ovle.rll3.model.ecs

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
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

fun entityWith(entities: Collection<Entity>, componentClass: KClass<out Component>) = entitiesWith(entities, componentClass).single()

fun Entity.component(componentClass: KClass<out Component>) = this[ComponentMapper.getFor(componentClass.java)]

inline fun <reified T: Component> singleComponent(entities: Collection<Entity>, componentClass: KClass<out T>) =
    entityWith(entities.toList(), componentClass).component(componentClass)!! as T

fun IteratingSystem.hostEntities() = this.entities
fun EntitySystem.allEntities() = this.engine.entities


fun EntitySystem.levelInfo() = singleComponent(allEntities().toList(), LevelComponent::class).level