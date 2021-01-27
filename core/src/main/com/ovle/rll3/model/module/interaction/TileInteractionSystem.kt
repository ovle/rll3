package com.ovle.rll3.model.module.interaction

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.component1
import com.ovle.rll3.component2
import com.ovle.rll3.event.Event.GameEvent.CheckTaskCommand
import com.ovle.rll3.event.Event.PlayerControlEvent.ClickEvent
import com.ovle.rll3.event.Event.PlayerControlEvent.DragEvent
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.entity.locationInfo
import com.ovle.rll3.model.module.core.entity.playerInteractionInfo
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.game.AreaInfo
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.util.Area
import com.ovle.rll3.points
import com.ovle.rll3.rectangle
import com.ovle.rll3.view.viewportToGame
import kotlin.math.max
import kotlin.math.min


class TileInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<ClickEvent> { onClickEvent(it.button, it.point) }
        EventBus.subscribe<DragEvent> { onDragEvent(it.start, it.current) }

//        EventBus.subscribe<EntityUseSkill> { onEntityUseSkillEvent(it.entity, it.target, it.skillTemplate) }
    }

    private fun onClickEvent(button: Int, point: GridPoint2) {
        val interactionInfo = playerInteractionInfo()!!
        if (interactionInfo.selectionMode != SelectionMode.Tile) return

        when (interactionInfo.controlMode) {
            ControlMode.Task -> {
                val selectionRectangle = interactionInfo.selectionRectangle ?: return
                val points = selectionRectangle.points()
                val target =
                    if (points.size == 1) TaskTarget(points.single())
                    else TaskTarget(Area(points.toHashSet()))
                EventBus.send(CheckTaskCommand(target))
            }
            ControlMode.Areas -> {
                //todo area system?
                val location = locationInfo()
                val existingArea = location.areas.find { it.area.points.contains(point) }
                if (existingArea != null) {
                    interactionInfo.selectedArea = existingArea
                } else {
                    //todo
                    val newArea = interactionInfo.selectionRectangle
                    newArea?.let {
                        location.areas += AreaInfo(Area(newArea.points().toMutableSet()))
                    }
                }
            }
        }
    }

    private fun onDragEvent(start: Vector2, current: Vector2) {
        val interactionInfo = playerInteractionInfo()!!
        if (interactionInfo.controlMode == ControlMode.View) return
        if (interactionInfo.selectionMode != SelectionMode.Tile) return

        val (sx, sy) = start.viewportToGame()
        val (cx, cy) = current.viewportToGame()

        val xRange = (min(sx, cx)..max(sx, cx))
        val yRange = (min(sy, cy)..max(sy, cy))

        interactionInfo.selectionRectangle = rectangle(xRange.first, yRange.first, xRange.last, yRange.last)
    }
}
