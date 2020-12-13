//package com.ovle.rll3.model.module.ai.condition
//
//import com.ovle.rll3.model.module.ai.BaseTask
//import com.ovle.rll3.model.module.core.component.ComponentMappers.position
//import ktx.ashley.get
//
//class IsNearPositionCondition: BaseTask() {
//
//    override fun executeIntr(): Status {
//        val currentPosition = owner[position]!!.gridPosition
//        val isSuccess = currentPosition.dst2(targetPosition) <= 1
//
//        return if (isSuccess) Status.SUCCEEDED else Status.FAILED
//    }
//}