//package com.ovle.rll3.model.module.ai.condition
//
//import com.ovle.rll3.model.module.ai.BaseTask
//import com.ovle.rll3.model.module.core.component.ComponentMappers.move
//import com.ovle.rll3.model.module.task.TaskTarget
//import ktx.ashley.get
//
//class IsMovingToPositionCondition: BaseTask() {
//
//    override fun executeIntr(): Status {
//        val moveComponent = owner[move]!!
//        val isMoving = moveComponent.path.started
////        val moveTarget = moveComponent.path.currentTarget ?: return Status.FAILED
////        val taskTarget = (target as TaskTarget.PositionTarget).position
////        val isMovingToTarget = moveTarget == taskTarget
//
//        return if (isMoving) Status.SUCCEEDED else Status.FAILED
//    }
//}