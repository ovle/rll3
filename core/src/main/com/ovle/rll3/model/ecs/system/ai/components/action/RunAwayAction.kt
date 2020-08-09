package com.ovle.rll3.model.ecs.system.ai.components.action

import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.entity.levelInfo
import com.ovle.rll3.model.ecs.system.ai.components.EntityTask
import com.ovle.rll3.model.tile.isPassable
import ktx.ashley.get

@OptIn(ExperimentalStdlibApi::class)
class RunAwayAction: EntityTask() {

    override fun execute(): Status {
        println("exec RunAwayAction")

        val moveComponent = currentEntity[Mappers.move] ?: return Status.FAILED
        if (moveComponent.path.started) return Status.RUNNING

        val level = levelInfo(entities.toTypedArray())!!
        val tiles = level.tiles
        val toPointCandidates = tiles.positions().filter { tiles.isPassable(it) }
        val target = toPointCandidates.randomOrNull() ?: return Status.FAILED

        EventBus.send(Event.GameEvent.EntityEvent.EntityStartMoveCommand(currentEntity, target))

        return Status.SUCCEEDED
    }
}