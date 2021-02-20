package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.EntitySystem
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.inSet
import org.kodein.di.singleton


val skillModule = DI.Module("skill") {
    bind<EntitySystem>().inSet() with singleton {
        SkillSystem()
    }
}