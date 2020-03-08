package com.ovle.rll3.model.ecs.system

import com.ovle.rll3.Event
import com.ovle.rll3.Event.GlobalGameEvent
import com.ovle.rll3.EventBus.receive
import com.ovle.rll3.EventBus.send
import com.ovle.rll3.model.ecs.component.WorldInfo
import com.ovle.rll3.model.ecs.entity.newWorld
import com.ovle.rll3.model.procedural.config.world


class GameSystem : EventSystem<GlobalGameEvent>() {

    override fun channel() = receive<GlobalGameEvent>()

    override fun dispatch(event: GlobalGameEvent) {
        when (event) {
            is Event.GameStartedEvent -> loadWorld(world)
            else -> {}
        }
    }

    private fun loadWorld(world: WorldInfo) {
        //todo
        val worldEntity = newWorld(world, engine)

        send(Event.WorldInitEvent(world))
    }
}
