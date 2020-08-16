package com.ovle.rll3.model.module.interaction

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.cartesianProduct
import com.ovle.rll3.component1
import com.ovle.rll3.component2
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.Event.GameEvent
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.Event.PlayerControlEvent.ClickEvent
import com.ovle.rll3.event.Event.PlayerControlEvent.DragEvent
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.entity.playerInteractionInfo
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.module.task.TaskTarget.*
import com.ovle.rll3.point
import com.ovle.rll3.view.viewportToGame
import kotlin.math.min
import kotlin.math.max


class TileInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<ClickEvent> { onClickEvent(it.button, it.point) }
        EventBus.subscribe<DragEvent> { onDragEvent(it.start, it.current) }

//        EventBus.subscribe<DebugChangeSelectedTiles> { onDebugChangeSelectedTilesEvent() }
//        EventBus.subscribe<EntityUseSkill> { onEntityUseSkillEvent(it.entity, it.target, it.skillTemplate) }
    }

    private fun onClickEvent(button: Int, point: GridPoint2) {
        val interactionInfo = playerInteractionInfo()!!
        if (interactionInfo.controlMode != ControlMode.Task) return
        if (interactionInfo.selectionMode != SelectionMode.Tile) return

        val selectedTiles = interactionInfo.selectedTiles
        if (selectedTiles.isNotEmpty()) {
            val target =
                if (selectedTiles.size == 1) PositionTarget(selectedTiles.single())
                else AreaTarget(selectedTiles)

            EventBus.send(CheckTaskCommand(target))
        }
    }

    private fun onDragEvent(start: Vector2, current: Vector2) {
        val interactionInfo = playerInteractionInfo()!!
        if (interactionInfo.controlMode != ControlMode.Task) return
        if (interactionInfo.selectionMode != SelectionMode.Tile) return

        val (sx, sy) = start.viewportToGame()
        val (cx, cy) = current.viewportToGame()

        val xRange = (min(sx, cx)..max(sx, cx))
        val yRange = (min(sy, cy)..max(sy, cy))

        val selectedTiles = cartesianProduct(xRange.toList(), yRange.toList())
            .map { (x, y) -> point(x, y) }
        interactionInfo.selectedTiles = selectedTiles.toSet()
    }

//    private fun onDebugChangeSelectedTilesEvent() {
//        val interactionInfo = playerInteractionInfo()!!
//        if (interactionInfo.selectionMode != SelectionMode.Tile) return
//
//        val tiles = levelInfo().tiles
//        val selectedTiles = interactionInfo.selectedTiles
//        selectedTiles.forEach {
//            val tile = tiles.get(it.x, it.y)
//            tile.typeId = typeId(tile)
//
//            send(Event.DebugTileChanged(tile, it))
//        }
//    }
}
