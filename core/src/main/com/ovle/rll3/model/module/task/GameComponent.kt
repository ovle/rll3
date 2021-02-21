package com.ovle.rll3.model.module.task

import com.badlogic.gdx.utils.Queue
import com.ovle.rlUtil.gdx.ashley.component.BaseComponent

class TasksComponent(
    val tasks: Queue<TaskInfo> = Queue(),
    val taskHistory: Queue<TaskInfo> = Queue()
): BaseComponent