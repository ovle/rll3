package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.template.TemplatesRegistry

fun moveTaskAction(e: Entity, t: TaskTarget) {
    t as TaskTarget.PositionTarget  //todo
    EventBus.send(Event.GameEvent.EntityStartMoveCommand(e, t.position))
}

fun gatherAction(e: Entity, t: TaskTarget) {
    t as TaskTarget.EntityTarget  //todo
    val st = TemplatesRegistry.skillTemplates["gather"] //todo
    EventBus.send(Event.GameEvent.EntityUseSkill(e, t.entity, st!!))
}

fun attackAction(e: Entity, t: TaskTarget) {
    t as TaskTarget.EntityTarget  //todo
    val st = TemplatesRegistry.skillTemplates["attack"] //todo
    EventBus.send(Event.GameEvent.EntityUseSkill(e, t.entity, st!!))
}