package com.ovle.rll3.model.module.ai

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.TemplatedState
import com.ovle.rll3.model.module.core.component.EntityComponent
import org.kodein.di.*


val aiModule = DI.Module("AI") {
    bind<EntitySystem>().inSet() with singleton {
        AISystem()
    }

    bind<EntityComponent>(tag = "ai") with factory { value: TemplatedState? ->
        AIComponent(behavior = (value?.get("behavior") as String?) ?: "base")
    }
}