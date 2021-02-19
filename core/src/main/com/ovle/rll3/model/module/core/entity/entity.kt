package com.ovle.rll3.model.module.core.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.EntityId
import com.ovle.rll3.model.module.core.component.CoreComponent
import com.ovle.rll3.model.module.space.PositionComponent
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.game.GameComponent
import com.ovle.rll3.model.module.interaction.PlayerInteractionComponent
import com.ovle.rll3.model.module.render.RenderComponent
import com.ovle.rll3.model.module.task.TasksComponent
import com.ovle.rll3.model.module.template.TemplateComponent
import com.ovle.rll3.model.module.time.TimeInfo
import com.ovle.rll3.model.procedural.grid.world.WorldInfo
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.model.template.entity.entityViewTemplate
import java.util.*

fun Engine.entity(id: EntityId, vararg components: Component) = createEntity().apply {
    this.add(CoreComponent(id))
    components.forEach { component -> this.add(component) }
    addEntity(this)
}

fun randomId() = UUID.randomUUID().toString()

fun newGame(location: LocationInfo, world: WorldInfo?, engine: Engine) = engine.entity(
    location.id,
    GameComponent(location, world, TimeInfo()), //todo separate
    TasksComponent()    //todo separate
)

fun newPlayerInteraction(engine: Engine): Entity? = engine.entity(
    "not used",
    PlayerInteractionComponent(),
    PositionComponent()
)

fun newTemplatedEntity(id: EntityId, template: EntityTemplate, gameEngine: Engine): Entity {
    val components = basicComponents(template) + stateComponents(template)
    return gameEngine.entity(id, *components.toTypedArray())
}

private fun basicComponents(template: EntityTemplate): List<Component> {
    val viewTemplate = entityViewTemplate(name = template.name)
    //todo items doesn't have anything but template
    return listOfNotNull(
        TemplateComponent(template, viewTemplate),
        viewTemplate?.sprite?.run { RenderComponent() },
    )
}

private fun stateComponents(template: EntityTemplate): List<Component> = listOf()
//    template.state.map { (k, v) ->
//        (componentsMapper[k] ?: error("no mapper found for key $k")).invoke(v as ComponentData?)
//    }