package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.TemplatedState
import com.ovle.rll3.model.module.core.component.BaseComponent
import org.kodein.di.*


val taskModule = DI.Module("task") {
    bind<EntitySystem>().inSet() with singleton {
        TaskSystem()
    }

    bind<BaseComponent>(tag = "task").inSet() with factory { _: TemplatedState? ->
        TaskPerformerComponent()
    }
}