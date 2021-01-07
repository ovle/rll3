package com.ovle.rll3.model.module.core.entity

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.core.component.ComponentMappers.taskPerformer
import ktx.ashley.get
import ktx.ashley.has

fun isTaskPerformer(e: Entity) = e.has(taskPerformer)
fun isFreeTaskPerformer(e: Entity) = isTaskPerformer(e) && e[taskPerformer]!!.current == null