package com.ovle.rll3.model.module.core.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.core.Components.core
import ktx.ashley.get
import ktx.ashley.has
import kotlin.reflect.KClass


//todo use families?
fun entitiesWith(entities: Collection<Entity>, componentClass: KClass<out Component>) =
    ComponentMapper.getFor(componentClass.java)
    .run {
        entities.filter {
            it.has(this) && it[core]!!.isExists
        }
    }

fun entityWith(entities: Collection<Entity>, componentClass: KClass<out Component>) = entitiesWith(entities, componentClass).singleOrNull()

fun Engine.allEntities() = entities.filter { it[core]!!.isExists }

