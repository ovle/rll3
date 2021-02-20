package com.ovle.rll3.model.module.game

import com.badlogic.ashley.core.EntitySystem
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.inSet
import org.kodein.di.singleton


val gameModule = DI.Module("game") {
    bind<EntitySystem>().inSet() with singleton {
        GameSystem()
    }
}