package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.TemplatedState
import com.ovle.rll3.model.module.core.component.EntityComponent
import com.ovle.rll3.model.module.core.component.GlobalComponent
import org.kodein.di.*


val taskModule = DI.Module("task") {
    bind<EntitySystem>().inSet() with singleton {
        TaskSystem()
    }

    bind<EntityComponent>(tag = "task") with factory { _: TemplatedState? ->
        TaskPerformerComponent()
    }

    bind<GlobalComponent>().inSet() with provider {
        TasksComponent()
    }
}