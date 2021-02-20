package com.ovle.rll3.model.module.resource

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.ComponentData
import com.ovle.rll3.ResourceAmount
import com.ovle.rll3.TemplatedState
import com.ovle.rll3.model.module.core.component.BaseComponent
import org.kodein.di.*


val resourceModule = DI.Module("resource") {
    bind<EntitySystem>().inSet() with singleton {
        ResourceSystem()
    }

    fun resourceType(value: ComponentData?) = ResourceType.valueOf((value!!["type"] as String).capitalize())

    bind<BaseComponent>(tag = "source") with factory { value: TemplatedState? ->
        SourceComponent(
            type = resourceType(value),
            amount = value!!["amount"] as ResourceAmount
        )
    }
    bind<BaseComponent>(tag = "resource") with factory { value: TemplatedState? ->
        ResourceComponent(type = resourceType(value))
    }
}