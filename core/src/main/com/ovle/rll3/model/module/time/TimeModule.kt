package com.ovle.rll3.model.module.time

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.model.module.core.component.GlobalComponent
import org.kodein.di.*


val timeModule = DI.Module("time") {
    bind<EntitySystem>().inSet() with singleton {
        TimeSystem()
    }
    bind<GlobalComponent>().inSet() with provider {
        TimeComponent(TimeInfo())
    }
}