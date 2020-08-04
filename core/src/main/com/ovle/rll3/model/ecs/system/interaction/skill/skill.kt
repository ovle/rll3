package com.ovle.rll3.model.ecs.system.interaction.skill

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.util.Mappers
import ktx.ashley.get


fun skill(source: Entity, target: Any?, skillTemplate: SkillTemplate) {
    val skill = skillTemplate.skillEffect
    val actionComponent = source[Mappers.action]!!

    actionComponent.current = {
        val amount = skillTemplate.skillEffectAmount(source)
        skill.invoke(source, target, amount)

        send(Event.EntityChanged(source))
        if (target is Entity) {
            send(Event.EntityChanged(target))
        }
    }
    actionComponent.timeLeft = skillTemplate.time

    //todo animation
}