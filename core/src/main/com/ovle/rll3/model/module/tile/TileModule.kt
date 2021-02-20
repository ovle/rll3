package com.ovle.rll3.model.module.tile

import com.badlogic.ashley.core.EntitySystem
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.inSet
import org.kodein.di.singleton


val tileModule = DI.Module("tile") {
    bind<EntitySystem>().inSet() with singleton {
        TileSystem()
    }
}