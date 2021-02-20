package com.ovle.rll3.model.module.controls

import com.badlogic.ashley.core.EntitySystem
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.inSet
import org.kodein.di.singleton


val controlsModule = DI.Module("controls") {
    bind<EntitySystem>().inSet() with singleton {
        PlayerControlsSystem()
    }
}