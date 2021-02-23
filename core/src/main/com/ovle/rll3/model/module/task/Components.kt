package com.ovle.rll3.model.module.task

import com.ovle.rlUtil.gdx.ashley.component.mapper

object Components {
    val tasks = mapper<TasksComponent>()
    val taskPerformer = mapper<TaskPerformerComponent>()
}