package com.ovle.rll3.model.module.task

import com.ovle.rll3.*
import com.ovle.rll3.model.module.ai.bt.BTTemplate

data class TaskTemplate (
    val performerFilter: TaskPerformerFilter,
    val targetFilter: TaskTargetFilter? = null,
    val targetMap: TaskTargetMap = { t, _ -> listOf(t) },
    val btTemplate: BTTemplate
)