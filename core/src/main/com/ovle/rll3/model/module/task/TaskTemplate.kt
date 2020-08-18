package com.ovle.rll3.model.module.task

import com.ovle.rll3.*


data class TaskTemplate (
    val performerFilter: TaskPerformerFilter,
    val targetFilter: TaskTargetFilter,
    val btName: String
)

//class TaskNode(
//    val template: TaskTemplate? = null,
//    val children: Array<TaskNode> = arrayOf()
//)

//
//sealed class TaskNode(val next: Array<TaskNode> = arrayOf()) {
//    class Root(next: Array<TaskNode>): TaskNode(next)
//    class Action(val taskTemplate: TaskTemplate, next: Array<TaskNode>): TaskNode(next)
//    class And(next: Array<TaskNode>): TaskNode(next)
//    class Or(next: Array<TaskNode>): TaskNode(next)
//    class Not(next: Array<TaskNode>): TaskNode(next)
//}