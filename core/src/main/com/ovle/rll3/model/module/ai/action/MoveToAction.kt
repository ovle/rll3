package com.ovle.rll3.model.module.ai.action

import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.Task.Status.*
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.IsAtPositionStrategy
import com.ovle.rll3.MoveStrategy
import com.ovle.rll3.event.Event.GameEvent.EntityStartMoveCommand
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.BaseBlackboard
import com.ovle.rll3.model.module.ai.BaseTask
import com.ovle.rll3.model.module.core.component.ComponentMappers.move
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.entity.locationInfo
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.task.EntityConditions.isAtPosition
import com.ovle.rll3.model.module.task.EntityConditions.isMoving
import com.ovle.rll3.model.module.task.EntityConditions.isNearPosition
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.util.pathfinding.aStar.path
import com.ovle.rll3.nearHV
import com.ovle.rll3.point
import ktx.ashley.get

//todo extract move strategy?
class MoveToAction: BaseTask() {

    @TaskAttribute(required = true)
    lateinit var type: String


    override fun executeIntr(): Status {
        checkNotNull(owner[position])
        checkNotNull(owner[move])

        return when {
            isFinished() -> SUCCEEDED
            isMoving(owner) -> RUNNING
            else -> {
                val from = owner[position]!!.gridPosition
                val level = locationInfo(entities.toTypedArray())
                checkNotNull(level)

                val moveStrategy = moveStrategy(target)
                val result = moveStrategy.invoke(from, targetPosition, level)

                if (result) RUNNING else FAILED
            }
        }
    }

    override fun copyTo(otherTask: Task<BaseBlackboard>): Task<BaseBlackboard> {
        return super.copyTo(otherTask).apply {
            otherTask as MoveToAction
            otherTask.type = type

            //todo
            if (type == "area-resource") {
                otherTask as BaseTask
                otherTask.customTarget = point(5, 82)
            }
        }
    }

    private fun isFinished() = isAtPositionStrategy(target).invoke(owner, targetPosition)

    private fun isAtPositionStrategy(target: TaskTarget): IsAtPositionStrategy =
        if (type == "exact") ::isAtPosition else ::isNearPosition

    private fun moveStrategy(target: TaskTarget): MoveStrategy =
        if (type == "exact") ::moveTo else ::moveNearTo

    private fun moveTo(from: GridPoint2, to: GridPoint2, location: LocationInfo): Boolean {
        val path = path(from, to, location)
        val isPathExists = path.isNotEmpty()
        if (isPathExists) {
            EventBus.send(EntityStartMoveCommand(owner, targetPosition))
        }
        println("move from $from to $to exists:$isPathExists")

        return isPathExists
    }

    private fun moveNearTo(from: GridPoint2, to: GridPoint2, location: LocationInfo): Boolean {
        val nearPoints = to.nearHV().sortedBy { it.dst(from) }
        var isPathExists = false

        for (it in nearPoints) {
            isPathExists = moveTo(from, it, location)
            if (isPathExists) {
                EventBus.send(EntityStartMoveCommand(owner, it))

                break
            }
        }

        return isPathExists
    }
}