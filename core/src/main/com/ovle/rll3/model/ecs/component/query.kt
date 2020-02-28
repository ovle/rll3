package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.entity.EntityQuery
import ktx.ashley.get
import ktx.ashley.has
import kotlin.reflect.KClass

object ComponentQuery {

    inline fun <reified T : Component> singleComponentNullable(entities: Collection<Entity>, componentClass: KClass<out T>) =
        EntityQuery.entityWithNullable(entities.toList(), componentClass)?.component(componentClass) as T?

    inline fun <reified T : Component> singleComponent(entities: Collection<Entity>, componentClass: KClass<out T>) =
        singleComponentNullable(entities, componentClass)!!
}

inline fun <reified T : Component> componentMapper(): ComponentMapper<T> = ComponentMapper.getFor(T::class.java)

fun Entity.has(componentClass: KClass<out Component>) = this.has(ComponentMapper.getFor(componentClass.java))

inline fun <reified T : Component> Entity.component(componentClass: KClass<out T>) = this[ComponentMapper.getFor(componentClass.java)]