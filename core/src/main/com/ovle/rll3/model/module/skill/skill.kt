package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.Mappers
import ktx.ashley.get


fun skill(source: Entity, target: Any?, skillTemplate: SkillTemplate) {
    val skill = skillTemplate.skillEffect
    val actionComponent = source[Mappers.action]!!

    actionComponent.current = {
        val amount = skillTemplate.skillEffectAmount(source)
        skill.invoke(source, target, amount)

        send(Event.GameEvent.EntityEvent.EntityChangedEvent(source))
        if (target is Entity) {
            send(Event.GameEvent.EntityEvent.EntityChangedEvent(target))
        }
    }
    actionComponent.timeLeft = skillTemplate.time

    //todo animation
}