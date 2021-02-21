package com.ovle.rll3.model.module.perception

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.TemplatedState
import com.ovle.rll3.model.module.core.component.EntityComponent
import org.kodein.di.*


val perceptionModule = DI.Module("perception") {
    bind<EntitySystem>().inSet() with singleton {
        PerceptionSystem()
    }

    bind<EntityComponent>(tag = "perception") with factory { value: TemplatedState? ->
        PerceptionComponent(value!!["sight"] as Int? ?: 5)
    }
}