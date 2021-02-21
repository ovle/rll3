package com.ovle.rll3.model.module.light

import com.ovle.rll3.TemplatedState
import com.ovle.rll3.model.module.core.component.EntityComponent
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory


val lightModule = DI.Module("light") {
    bind<EntityComponent>(tag = "light") with factory { value: TemplatedState? ->
        LightSourceComponent(AOEData(value!!["radius"] as Int))
    }
}