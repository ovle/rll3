package com.ovle.rll3.model.module.core.component

import com.ovle.rll3.EntityId
import com.ovle.rll3.TemplatedState
import com.ovle.rll3.di
import com.ovle.rll3.model.module.core.component.template.TemplateComponent
import com.ovle.rll3.model.module.game.GameComponent
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.procedural.grid.world.WorldInfo
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.model.template.entity.entityViewTemplate
import org.kodein.di.direct
import org.kodein.di.factoryOrNull
import org.kodein.di.instance

fun basicComponents(id: EntityId, template: EntityTemplate): List<EntityComponent> {
    val coreComponent = CoreComponent(id)
    val templateComponent = TemplateComponent(template, entityViewTemplate(name = template.name))
    val components: Set<EntityComponent> = di.direct.instance()
    return listOfNotNull(
        coreComponent,
        templateComponent,
        *components.toTypedArray()
    )
}

fun globalComponents(location: LocationInfo, world: WorldInfo?): List<GlobalComponent> {
    val gameComponent = GameComponent(location, world)
    val components: Set<GlobalComponent> = di.direct.instance()
    return listOfNotNull(
        gameComponent,
        *components.toTypedArray()
    )
}

fun templatedComponents(template: EntityTemplate): List<EntityComponent> =
    template.state.map { (k, v) ->
        val componentFactory = di.direct.factoryOrNull<TemplatedState, EntityComponent>(k)
        checkNotNull(componentFactory) { "no component factory found for key $k, template ${template.name}" }
        val component = componentFactory.invoke((v ?: emptyMap<String, Any?>()) as TemplatedState)
        component
    }