package com.ovle.rll3.model.module.space

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.TemplatedState
import com.ovle.rll3.model.module.core.component.EntityComponent
import org.kodein.di.*


val spaceModule = DI.Module("space") {
    bind<EntitySystem>().inSet() with singleton {
        MoveSystem()
    }

    bind<EntityComponent>().inSet() with provider {
        PositionComponent()
    }

    bind<EntityComponent>(tag = "move") with factory { _: TemplatedState? ->
        MoveComponent()
    }
}