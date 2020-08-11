package com.ovle.rll3.model.module.ai.action

import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.core.entity.levelInfo
import com.ovle.rll3.model.module.ai.EntityTask
import com.ovle.rll3.model.tile.isPassable
import ktx.ashley.get

@OptIn(ExperimentalStdlibApi::class)
class RunAwayAction: EntityTask() {

    override fun execute(): Status {
        println("exec RunAwayAction")

        val moveComponent = currentEntity[ComponentMappers.move] ?: return Status.FAILED
        if (moveComponent.path.started) return Status.RUNNING

        val level = levelInfo(entities.toTypedArray())!!
        val tiles = level.tiles
        val toPointCandidates = tiles.positions().filter { tiles.isPassable(it) }
        val target = toPointCandidates.randomOrNull() ?: return Status.FAILED

        EventBus.send(Event.GameEvent.EntityEvent.EntityStartMoveCommand(currentEntity, target))

        return Status.SUCCEEDED
    }
}