package com.ovle.rll3.model.module.entityAction

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.model.module.core.component.EntityComponent
import org.kodein.di.*


val entityActionModule = DI.Module("entityAction") {
    bind<EntitySystem>().inSet() with singleton {
        EntityActionSystem()
    }

    bind<EntityComponent>().inSet() with provider {
        EntityActionComponent()
    }
}