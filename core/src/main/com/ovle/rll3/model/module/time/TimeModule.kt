package com.ovle.rll3.model.module.time

import com.badlogic.ashley.core.EntitySystem
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.inSet
import org.kodein.di.singleton


val timeModule = DI.Module("time") {
    bind<EntitySystem>().inSet() with singleton {
        TimeSystem()
    }
}