package com.ovle.rll3.model.module.core.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.EntityId
import com.ovle.rll3.model.module.core.component.ComponentMappers.id
import com.ovle.rll3.model.module.core.component.ComponentMappers.perception
import com.ovle.rll3.model.module.core.component.print
import com.ovle.rll3.view.noVisibilityFilter
import ktx.ashley.get
import ktx.ashley.has

fun Collection<Entity>.print() = map { it.print() }.foldRight("entities: ") { acc, it -> "$acc; $it" }

fun Entity.print() = components.map { it.print() }.foldRight("components: ") { acc, it -> "$acc, $it" }

fun Entity.see(position: GridPoint2): Boolean {
    check(this.has(perception))

    val fov = this[perception]!!.fov
    return noVisibilityFilter || position in fov
}

fun Entity.id(): EntityId {
    check(this.has(id))
    return this[id]!!.id
}
