package com.ovle.rll3.model.ecs.entity

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.basic.TaskPerformerComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.taskPerformer
import com.ovle.rll3.model.ecs.component.util.has
import ktx.ashley.get

fun anyEntity(e: Entity) = true
fun anyTaskPerformer(e: Entity) = e.has<TaskPerformerComponent>()
fun freeTaskPerformer(e: Entity) = anyTaskPerformer(e) && e[taskPerformer]!!.current == null