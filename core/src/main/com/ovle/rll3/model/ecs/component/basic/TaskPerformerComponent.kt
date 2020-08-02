package com.ovle.rll3.model.ecs.component.basic

import com.ovle.rll3.model.ecs.component.BaseComponent
import com.ovle.rll3.model.ecs.component.dto.TaskInfo

class TaskPerformerComponent(
    var current: TaskInfo? = null
) : BaseComponent