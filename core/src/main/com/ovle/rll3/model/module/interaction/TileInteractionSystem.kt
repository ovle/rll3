package com.ovle.rll3.model.module.interaction

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rlUtil.gdx.controls.ClickEvent
import com.ovle.rlUtil.gdx.controls.DragEvent
import com.ovle.rlUtil.gdx.math.*
import com.ovle.rll3.event.CheckTaskCommand
import com.ovle.rll3.model.module.core.entity.locationInfo
import com.ovle.rll3.model.module.core.entity.playerInteractionInfo
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.game.AreaInfo
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.util.viewportToGame
import kotlin.math.max
import kotlin.math.min


class TileInteractionSystem : EventSystem() {

    override fun subscribe() {
        subscribe<ClickEvent> { onClickEvent(it.button, it.point) }
        subscribe<DragEvent> { onDragEvent(it.start, it.current) }

//        EventBus.subscribe<EntityUseSkill> { onEntityUseSkillEvent(it.entity, it.target, it.skillTemplate) }
    }

    private fun onClickEvent(button: Int, point: GridPoint2) {
        val interactionInfo = engine.playerInteractionInfo()!!
        if (interactionInfo.selectionMode != SelectionMode.Tile) return

        when (interactionInfo.controlMode) {
            ControlMode.Task -> {
                val selectionRectangle = interactionInfo.selectionRectangle ?: return
                val points = selectionRectangle.points()
                val target =
                    if (points.size == 1) TaskTarget(points.single())
                    else TaskTarget(Area(points.toHashSet()))
                send(CheckTaskCommand(target))
            }
            ControlMode.Areas -> {
                //todo area system?
                val location = engine.locationInfo()!!
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
        val interactionInfo = engine.playerInteractionInfo()!!
        if (interactionInfo.controlMode == ControlMode.View) return
        if (interactionInfo.selectionMode != SelectionMode.Tile) return

        val (sx, sy) = start.viewportToGame()
        val (cx, cy) = current.viewportToGame()

        val xRange = (min(sx, cx)..max(sx, cx))
        val yRange = (min(sy, cy)..max(sy, cy))

        interactionInfo.selectionRectangle = rectangle(xRange.first, yRange.first, xRange.last, yRange.last)
    }
}
