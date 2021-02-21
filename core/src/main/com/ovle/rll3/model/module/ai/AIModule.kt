package com.ovle.rll3.model.module.ai

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rlUtil.gdx.ashley.component.BaseComponent
import com.ovle.rll3.TemplatedState
import org.kodein.di.*


val aiModule = DI.Module("AI") {
    bind<EntitySystem>().inSet() with singleton {
        AISystem()
    }

    bind<BaseComponent>(tag = "ai") with factory { value: TemplatedState? ->
        AIComponent(behavior = (value?.get("behavior") as String?) ?: "base")
    }
}