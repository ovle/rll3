package com.ovle.rll3.model.module.tile

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.Tile
import com.ovle.rll3.Turn
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.EventBus.subscribe
import com.ovle.rll3.model.module.core.entity.locationInfo
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.task.TileConditions.isSource


class TileSystem : EventSystem() {

    override fun subscribe() {
        subscribe<ChangeTileCommand> { onChangeTileCommand(it.tile, it.position) }
        subscribe<TurnChangedEvent> { onTimeChanged(it.turn) }
    }

    private fun onChangeTileCommand(tileId: Tile, position: GridPoint2) {
        val locationInfo = locationInfo()
        val tiles = locationInfo.tiles

        val oldTileId = tiles[position.x, position.y]
        tiles[position.x, position.y] = tileId

        send(TileChangedEvent(tileId, position))

        if (isSource(oldTileId)) {
            send(TileGatheredEvent(oldTileId, position))
        }
    }

    private fun onTimeChanged(turn: Turn) {
        //todo
    }
}