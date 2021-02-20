package com.ovle.rll3.model.module.interaction

import com.badlogic.ashley.core.EntitySystem
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.inSet
import org.kodein.di.singleton


val interactionModule = DI.Module("interaction") {
    bind<EntitySystem>().inSet() with singleton {
        BaseInteractionSystem()
    }
    bind<EntitySystem>().inSet() with singleton {
        EntityInteractionSystem()
    }
    bind<EntitySystem>().inSet() with singleton {
        TileInteractionSystem()
    }
}