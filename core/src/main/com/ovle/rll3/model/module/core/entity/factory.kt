package com.ovle.rll3.model.module.core.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.EntityId
import com.ovle.rll3.model.module.core.component.CoreComponent
import com.ovle.rll3.model.module.core.component.basicComponents
import com.ovle.rll3.model.module.core.component.globalComponents
import com.ovle.rll3.model.module.core.component.templatedComponents
import com.ovle.rll3.model.module.game.dto.LocationInfo
import com.ovle.rll3.model.procedural.grid.world.WorldInfo
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.model.util.randomId


fun Engine.newEntity(id: EntityId, vararg components: Component) = createEntity().apply {
    this.add(CoreComponent(id))
    components.forEach { component -> this.add(component) }
    addEntity(this)
}

fun newGame(location: LocationInfo, world: WorldInfo?, engine: Engine): Entity {
    val components = globalComponents(location, world)
    return engine.newEntity(
        randomId(),
        *components.toTypedArray()
    )
}

fun newTemplatedEntity(id: EntityId, template: EntityTemplate, gameEngine: Engine): Entity {
    val components = basicComponents(id, template) + templatedComponents(template)
    return gameEngine.newEntity(id, *components.toTypedArray())
}