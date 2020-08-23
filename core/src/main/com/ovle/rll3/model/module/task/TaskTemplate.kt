package com.ovle.rll3.model.module.task

import com.ovle.rll3.*

//todo is this class necessary?
data class TaskTemplate (
    val performerFilter: TaskPerformerFilter,
    val targetFilter: TaskTargetFilter? = null,
    val btName: String
)