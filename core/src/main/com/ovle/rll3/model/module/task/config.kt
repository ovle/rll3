//package com.ovle.rll3.model.module.task
//
//import com.ovle.rll3.model.module.core.entity.anyTaskPerformer
//import com.ovle.rll3.model.module.task.EntityConditions.isLivingEntity
//import com.ovle.rll3.model.module.task.EntityConditions.isSourceEntity
//
//
//val moveToTaskTemplate = TaskTemplate(
//    performerFilter = ::anyTaskPerformer,
//    btName = "moveTo"
//)
//
//val gatherTaskTemplate = TaskTemplate(
//    performerFilter = ::anyTaskPerformer,
//    targetFilter = { isEntityTarget(it) && isSourceEntity(it.asEntityTarget().entity) },
//    btName = "gather"
//)
//
//val attackTaskTemplate = TaskTemplate(
//    performerFilter = ::anyTaskPerformer,
//    targetFilter = { isEntityTarget(it) && isLivingEntity(it.asEntityTarget().entity)},
//    btName = "attack"
//)
//
////val digTaskTemplate = TaskTemplate(
////    performerFilter = ::freeTaskPerformer,
////    targetFilter = ::isPositionCondition,
//////    oneTimeAction = ::moveTaskAction,
//////    successCondition = ::isAtPositionCondition
////)
//
//
//
//fun isPositionTarget(t: TaskTarget): Boolean = t is TaskTarget.PositionTarget
//
//fun isEntityTarget(t: TaskTarget): Boolean = t is TaskTarget.EntityTarget
//fun TaskTarget.asEntityTarget() = (this as TaskTarget.EntityTarget)
//
//fun taskTemplates() = arrayOf(gatherTaskTemplate, attackTaskTemplate, moveToTaskTemplate)