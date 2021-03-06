package com.ovle.rll3.model.ecs

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.component.util.Mappers.id
import com.ovle.rll3.model.ecs.component.util.Mappers.perception
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.system.level.EntityId
import com.ovle.rll3.view.noVisibilityFilter
import ktx.ashley.get
import ktx.ashley.has
import kotlin.reflect.KClass

fun Collection<Entity>.print() = map { it.print() }.foldRight("entities: ") { acc, it -> "$acc; $it" }

fun Entity.print() = components.map { it.print() }.foldRight("components: ") { acc, it -> "$acc, $it" }

fun Component.print(): String {
    return this.toString()
}

fun Entity.see(position: GridPoint2): Boolean {
    check(this.has(perception))

    val fov = this[perception]!!.fov
    return noVisibilityFilter || position in fov
}

fun Entity.id(): EntityId {
    check(this.has(id))
    return this[id]!!.id
}
