package com.ovle.rll3.model.module.task

import com.ovle.rll3.model.module.task.dto.TaskTarget

class InvalidTargetException(val target: TaskTarget): RuntimeException()