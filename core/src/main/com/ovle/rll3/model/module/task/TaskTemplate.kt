package com.ovle.rll3.model.module.task

import com.ovle.rll3.*

data class TaskTemplate (
    val performerFilter: TaskPerformerFilter,
    val targetFilter: TaskTargetFilter,
    val oneTimeAction: TaskAction? = null,
    val everyTurnAction: TaskAction? = null,
    val successCondition: SuccessCondition,
    val failCondition: FailCondition? = null
)