package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.time.ticksInTurn
import ktx.ashley.get


fun skill(source: Entity, target: Any?, skillTemplate: SkillTemplate) {
    val effect = skillTemplate.skillEffect
    val actionComponent = source[ComponentMappers.entityAction]!!

    actionComponent.current = {
        val amount = skillTemplate.skillEffectAmount(source)
        effect.invoke(source, target, amount)

        send(Event.GameEvent.EntityChangedEvent(source))
        if (target is Entity) {
            send(Event.GameEvent.EntityChangedEvent(target))
        }
    }

    actionComponent.timeLeft = skillTemplate.turns * ticksInTurn
    //todo animation
}