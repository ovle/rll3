package com.ovle.rll3.model.module.core.entity

import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import ktx.ashley.has

fun anyEntity(e: Entity) = true
//fun anyTaskPerformer(e: Entity) = e.has(taskPerformer)
//fun freeTaskPerformer(e: Entity) = anyTaskPerformer(e) && e[taskPerformer]!!.current == null