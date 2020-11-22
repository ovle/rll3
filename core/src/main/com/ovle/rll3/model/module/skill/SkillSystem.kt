package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.time.ticksInTurn
import ktx.ashley.get

class SkillSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntityUseSkillCommand> { onEntityUseSkillCommand(it.source, it.target, it.skillTemplate) }
    }

    private fun onEntityUseSkillCommand(source: Entity, target: Any?, skillTemplate: SkillTemplate) {
        useSkill(source, target, skillTemplate)
    }

    //todo interrupt? (action.current = null)
    //todo animation on every use?
    private fun useSkill(source: Entity, target: Any?, skillTemplate: SkillTemplate) {
        val effect = skillTemplate.effect
        val actionComponent = source[ComponentMappers.entityAction]!!
        if (actionComponent.current != null) return

        //todo non-skill actions?
        actionComponent.current = {
            val amount = skillTemplate.effectAmount(source)
            effect.invoke(source, target, amount)

            send(EntityChangedEvent(source))
            if (target is Entity) {
                send(EntityChangedEvent(target))
            }
            send(EntityFinishUseSkillEvent(source, target, skillTemplate, amount))
        }

        actionComponent.timeLeft = skillTemplate.turns * ticksInTurn
        send(EntityStartUseSkillEvent(source, target, skillTemplate))
    }
}