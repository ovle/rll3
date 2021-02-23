package com.ovle.rll3.model.module.task

import com.ovle.rll3.model.module.core.component.EntityComponent
import com.ovle.rll3.model.module.task.dto.TaskInfo

class TaskPerformerComponent(
    var current: TaskInfo? = null
) : EntityComponent()