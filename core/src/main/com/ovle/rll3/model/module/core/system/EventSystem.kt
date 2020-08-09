package com.ovle.rll3.model.module.core.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem


abstract class EventSystem: EntitySystem() {

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        subscribe()
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
    }

    abstract fun subscribe()
}