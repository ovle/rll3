package com.ovle.rll3.model.module.render

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.TemplatedState
import com.ovle.rll3.model.module.core.component.BaseComponent
import org.kodein.di.*


val renderModule = DI.Module("render") {
    bind<EntitySystem>().inSet() with singleton {
        CameraSystem(instance())
    }
    bind<EntitySystem>().inSet() with singleton {
        RenderLocationSystem(instance(), instance(), instance())
    }
    bind<EntitySystem>().inSet() with singleton {
        RenderObjectsSystem(instance(), instance())
    }
    bind<EntitySystem>().inSet() with singleton {
        AnimationSystem()
    }

    bind<BaseComponent>(tag = "render").inSet() with factory { _: TemplatedState? ->
        RenderComponent()
    }
}