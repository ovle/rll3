package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rlUtil.gdx.ashley.component.BaseComponent
import com.ovle.rll3.TemplatedState
import org.kodein.di.*


val taskModule = DI.Module("task") {
    bind<EntitySystem>().inSet() with singleton {
        TaskSystem()
    }

    bind<BaseComponent>(tag = "task") with factory { _: TemplatedState? ->
        TaskPerformerComponent()
    }
}