package com.ovle.rll3.model.module.tile

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.Tile
import com.ovle.rll3.Turn
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.EventBus.subscribe
import com.ovle.rll3.model.module.core.entity.locationInfo
import com.ovle.rll3.model.module.core.system.EventSystem


class TileSystem : EventSystem() {

    override fun subscribe() {
        subscribe<ChangeTileCommand> { onChangeTileCommand(it.tile, it.position) }
        subscribe<TimeChangedEvent> { onTimeChanged(it.turn) }
    }

    private fun onChangeTileCommand(tileId: Tile, position: GridPoint2) {
        val locationInfo = locationInfo()

        locationInfo.tiles[position.x, position.y] = tileId
        send(TileChangedEvent(tileId, position))
    }

    private fun onTimeChanged(turn: Turn) {
        //todo
    }
}