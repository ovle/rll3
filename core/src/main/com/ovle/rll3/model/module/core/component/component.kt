package com.ovle.rll3.model.module.core.component

import com.badlogic.ashley.core.Component
import com.ovle.rlUtil.gdx.ashley.component.BaseComponent
import com.ovle.rll3.EntityId
import com.ovle.rll3.TemplatedState
import com.ovle.rll3.di
import com.ovle.rll3.model.module.core.component.template.TemplateComponent
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.model.template.entity.entityViewTemplate
import org.kodein.di.direct
import org.kodein.di.factoryOrNull
import org.kodein.di.instance

fun basicComponents(id: EntityId, template: EntityTemplate): List<Component> {
    val coreComponent = CoreComponent(id)
    val templateComponent = TemplateComponent(template, entityViewTemplate(name = template.name))
    val components: Set<BaseComponent> = di.direct.instance()
    return listOfNotNull(
        coreComponent,
        templateComponent,
        *components.toTypedArray()
    )
}

fun templatedComponents(template: EntityTemplate): List<Component> =
    template.state.map { (k, v) ->
        val componentFactory = di.direct.factoryOrNull<TemplatedState, BaseComponent>(k)
        checkNotNull(componentFactory) { "no component factory found for key $k, template ${template.name}" }
        val component = componentFactory.invoke((v ?: emptyMap<String, Any?>()) as TemplatedState)
        component
    }