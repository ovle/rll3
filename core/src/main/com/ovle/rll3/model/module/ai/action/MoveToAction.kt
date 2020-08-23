package com.ovle.rll3.model.module.ai.action

import com.badlogic.gdx.ai.btree.Task.Status.*
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.IsAtPositionStrategy
import com.ovle.rll3.MoveStrategy
import com.ovle.rll3.event.Event.GameEvent.EntityStartMoveCommand
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.BaseTask
import com.ovle.rll3.model.module.core.component.ComponentMappers.move
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.entity.levelInfo
import com.ovle.rll3.model.module.game.LevelInfo
import com.ovle.rll3.model.module.task.EntityConditions.isAtPosition
import com.ovle.rll3.model.module.task.EntityConditions.isMoving
import com.ovle.rll3.model.module.task.EntityConditions.isNearPosition
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.util.pathfinding.aStar.path
import com.ovle.rll3.nearHV
import ktx.ashley.get


class MoveToAction: BaseTask() {

    override fun executeIntr(): Status {
        checkNotNull(owner[position])
        checkNotNull(owner[move])

        return when {
            isAtPositionStrategy(target).invoke(owner, targetPosition) -> SUCCEEDED
            isMoving(owner) -> RUNNING
            else -> {
                val from = owner[position]!!.gridPosition
                val level = levelInfo(entities.toTypedArray())
                checkNotNull(level)

                val moveStrategy = moveStrategy(target)
                val result = moveStrategy.invoke(from, targetPosition, level)
                println("executeIntr move from $from to $targetPosition result:$result")

                if (result) RUNNING else FAILED
            }
        }
    }

    //todo i.e. 'break wall' task will have position target, but need to have isNear strategy
    private fun isAtPositionStrategy(target: TaskTarget): IsAtPositionStrategy =
        if (target is TaskTarget.PositionTarget) ::isAtPosition else ::isNearPosition

    private fun moveStrategy(target: TaskTarget): MoveStrategy =
        if (target is TaskTarget.PositionTarget) ::moveTo else ::moveNearTo

    private fun moveTo(from: GridPoint2, to: GridPoint2, level: LevelInfo): Boolean {
        val path = path(from, to, level)
        val isPathExists = path.isNotEmpty()
        if (isPathExists) {
            EventBus.send(EntityStartMoveCommand(owner, targetPosition))
        }
        println("move from $from to $to exists:$isPathExists")

        return isPathExists
    }

    private fun moveNearTo(from: GridPoint2, to: GridPoint2, level: LevelInfo): Boolean {
        val nearPoints = nearHV(to).sortedBy { it.dst(from) }
        var isPathExists = false

        for (it in nearPoints) {
            isPathExists = moveTo(from, it, level)
            if (isPathExists) {
                EventBus.send(EntityStartMoveCommand(owner, it))

                break
            }
        }

        return isPathExists
    }
}