package com.ovle.rll3.model.ecs.system

import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.EventBus.subscribe
import com.ovle.rll3.model.ecs.component.special.WorldInfo
import com.ovle.rll3.model.ecs.entity.newWorld
import com.ovle.rll3.model.procedural.config.world


class GameSystem : EventSystem() {

    override fun subscribe() {
        subscribe<Event.GameStartedEvent> { loadWorld(world) }
    }

    private fun loadWorld(world: WorldInfo) {
        //todo
        val worldEntity = newWorld(world, engine)

        send(Event.WorldInitEvent(world))
    }
}
