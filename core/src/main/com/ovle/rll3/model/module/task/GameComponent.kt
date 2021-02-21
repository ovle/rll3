package com.ovle.rll3.model.module.task

import com.badlogic.gdx.utils.Queue
import com.ovle.rll3.model.module.core.component.GlobalComponent

class TasksComponent(
    val tasks: Queue<TaskInfo> = Queue(),
    val taskHistory: Queue<TaskInfo> = Queue()
): GlobalComponent()