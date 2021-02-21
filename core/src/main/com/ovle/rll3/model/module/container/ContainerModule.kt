package com.ovle.rll3.model.module.container

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rlUtil.gdx.ashley.component.BaseComponent
import com.ovle.rll3.TemplatedState
import org.kodein.di.*


val containerModule = DI.Module("container") {
    //        ContainerSystem()
    bind<EntitySystem>().inSet() with singleton {
        CarrierSystem()
    }

    bind<BaseComponent>(tag = "container") with factory { _: TemplatedState? ->
        ContainerComponent()
    }
    bind<BaseComponent>(tag = "carrier") with factory { _: TemplatedState? ->
        CarrierComponent()
    }
}