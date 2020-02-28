package com.ovle.rll3.model.ecs

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import ktx.ashley.has
import kotlin.reflect.KClass

fun Collection<Entity>.print() = map { it.print() }.foldRight("entities: ") { acc, it -> "$acc; $it" }

fun Entity.print() = components.map { it.print() }.foldRight("components: ") { acc, it -> "$acc, $it" }

fun Component.print(): String {
    return this.toString()
}