package com.ovle.rll3.model.ecs.system

import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.EventBus.subscribe
import com.ovle.rll3.model.ecs.component.special.PlayerInfo
import com.ovle.rll3.model.ecs.component.special.WorldInfo
import com.ovle.rll3.model.ecs.entity.newPlayer
import com.ovle.rll3.model.ecs.entity.newWorld


class GameSystem : EventSystem() {

    override fun subscribe() {
        subscribe<Event.GameStartedEvent> { loadWorld(it.player, it.world) }
    }

    private fun loadWorld(player: PlayerInfo, world: WorldInfo) {
        val worldEntity = newWorld(world, engine)
        val playerEntity = newPlayer(player, engine)

        send(Event.WorldInitEvent(world))
    }
}
