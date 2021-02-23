package com.ovle.rll3.model.module.core.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.EntityId
import com.ovle.rll3.model.module.core.Components.core
import com.ovle.rll3.model.module.core.Components.template
import com.ovle.rll3.model.module.core.component.print
import com.ovle.rll3.model.util.info
import ktx.ashley.get
import ktx.ashley.has

//todo modules
import com.ovle.rll3.model.module.life.Components.life
import com.ovle.rll3.model.module.perception.Components.perception
import com.ovle.rll3.model.module.resource.Components.resource
import com.ovle.rll3.model.module.resource.ResourceType
import com.ovle.rll3.model.module.space.Components.position


fun Collection<Entity>.print() = map { it.print() }.foldRight("entities: ") { acc, it -> "$acc; $it" }

fun Entity.print() = components.map { it.print() }.foldRight("components: ") { acc, it -> "$acc, $it" }


fun Entity.see(position: GridPoint2): Boolean {
    check(this.has(perception)) { "no perception for entity ${this.info()}" }

    val fov = this[perception]!!.fov
    return position in fov
//    return noVisibilityFilter || position in fov
}

fun Entity.position(): GridPoint2 {
    check(this.has(position)) { "no position for entity ${this.info()}" }
    return this[position]!!.gridPosition
}

fun Entity.positionOrNull(): GridPoint2? {
    return this[position]?.gridPosition
}

fun Entity.setPosition(newPosition: GridPoint2) {
    check(this.has(position)) { "no position for entity ${this.info()}" }
    this[position]!!.gridPosition = newPosition
}

fun Entity.id(): EntityId {
    check(this.has(core)) { "no id for entity ${this.info()}" }
    return this[core]!!.id
}

fun Entity.name(): EntityId {
    check(this.has(template)) { "no template for entity ${this.info()}" }
    return this[template]!!.template.name
}

fun Entity.consumes(e: Entity): Boolean {
    check(this.has(life)) { "no life for entity ${this.info()}" }
    if (!e.has(resource)) return false

    //todo consumer-specific
    val foodTypes = setOf(ResourceType.Food)
    return e[resource]!!.type in foodTypes
}