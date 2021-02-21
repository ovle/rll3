package com.ovle.rll3.model.module.interaction

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.model.module.core.component.GlobalComponent
import org.kodein.di.*


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

    bind<GlobalComponent>().inSet() with provider {
        PlayerInteractionComponent()
    }
}