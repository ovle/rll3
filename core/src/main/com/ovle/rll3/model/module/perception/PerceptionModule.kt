package com.ovle.rll3.model.module.perception

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rlUtil.gdx.ashley.component.BaseComponent
import com.ovle.rll3.TemplatedState
import org.kodein.di.*


val perceptionModule = DI.Module("perception") {
    bind<EntitySystem>().inSet() with singleton {
        PerceptionSystem()
    }

    bind<BaseComponent>(tag = "perception") with factory { value: TemplatedState? ->
        PerceptionComponent(value!!["sight"] as Int? ?: 5)
    }
}