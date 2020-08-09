package com.ovle.rll3.model.module.core.entity

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.task.TaskPerformerComponent
import com.ovle.rll3.model.module.core.component.Mappers.taskPerformer
import com.ovle.rll3.model.module.core.component.has
import ktx.ashley.get

fun anyEntity(e: Entity) = true
fun anyTaskPerformer(e: Entity) = e.has<TaskPerformerComponent>()
fun freeTaskPerformer(e: Entity) = anyTaskPerformer(e) && e[taskPerformer]!!.current == null