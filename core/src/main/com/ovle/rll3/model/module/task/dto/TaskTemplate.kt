package com.ovle.rll3.model.module.task.dto

import com.ovle.rll3.*
import com.ovle.rll3.model.module.ai.behavior.BTTemplate

data class TaskTemplate (
    val performerFilter: TaskPerformerFilter,
    val targetFilter: TaskTargetFilter? = null,
    val targetMap: TaskTargetMap = { t, _ -> listOf(t) },
    val btTemplate: BTTemplate
)