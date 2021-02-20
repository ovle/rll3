package com.ovle.rll3.model.module.collision

import com.ovle.rll3.TemplatedState
import com.ovle.rll3.model.module.core.component.BaseComponent
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory


val collisionModule = DI.Module("collision") {
    bind<BaseComponent>(tag = "collision") with factory { value: TemplatedState? ->
        CollisionComponent(
            passable4Body = value?.get("passable4Body") as Boolean? ?: false,
            passable4Light = value?.get("passable4Light") as Boolean? ?: true
        )
    }
}