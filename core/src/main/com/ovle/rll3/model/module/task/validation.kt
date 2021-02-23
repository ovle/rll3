package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.module.task.Components.taskPerformer
import com.ovle.rll3.model.module.task.EntityConditions.isDead
import com.ovle.rll3.model.module.task.EntityConditions.isExists
import com.ovle.rll3.model.module.task.TaskStatus.*
import ktx.ashley.has


fun isValid(taskInfo: TaskInfo): Boolean {
    val isTargetValid = isValidTarget(taskInfo.target)

    return when (taskInfo.status) {
        Waiting -> isTargetValid
        else -> true    //todo
    }
}

fun checkValid(target: TaskTarget) {
    if (!isValidTarget(target)) throw InvalidTargetException(target)
}

fun isValidTarget(target: TaskTarget) = with(target.target) {
    when (this) {
        null -> true
        is Entity -> isExists(this)
        is GridPoint2 -> true
        else -> throw IllegalStateException("isValidTarget not supported for target $target")
    }
}

fun isValidPerformer(e: Entity) = isExists(e) && !isDead(e) && e.has(taskPerformer)

//todo step target becomes invalid during task perform (for tasks in process)

//todo if skill success is destroying entity then !isExists(t.asEntity()) check will throw an exception
//todo defaultPerformerFilter - havePathToAdjPosition - TaskTarget.position for example
//    fun validate() {
//        if (!isValidTarget(this)) throw InvalidTargetException()
//    }
