package com.ovle.rll3.model.module.entityAction

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rlUtil.gdx.ashley.component.BaseComponent
import org.kodein.di.*


val entityActionModule = DI.Module("entityAction") {
    bind<EntitySystem>().inSet() with singleton {
        EntityActionSystem()
    }

    bind<BaseComponent>().inSet() with provider {
        EntityActionComponent()
    }
}