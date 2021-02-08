package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.ovle.rlUtil.event.EventBus
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rll3.event.EntityChangedEvent
import com.ovle.rll3.event.EntityFinishUseSkillEvent
import com.ovle.rll3.event.EntityStartUseSkillEvent
import com.ovle.rll3.event.EntityUseSkillCommand
import com.ovle.rll3.model.module.core.component.ComponentMappers.entityAction
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.skill.CostStatus.*
import com.ovle.rll3.model.util.info
import ktx.ashley.get

class SkillSystem : EventSystem() {

    override fun subscribe() {
        subscribe<EntityUseSkillCommand> { onEntityUseSkillCommand(it.info) }
    }

    private fun onEntityUseSkillCommand(info: SkillUsage) {
        useSkill(info)
    }

    //todo animation on every use?
    private fun useSkill(info: SkillUsage) {
        val (skill, source, target, _) = info
        val (name, cost, _, turns, effect, effectAmount, isSuccess) = skill

        val actionComponent = source[entityAction]!!

        //todo when to interrupt? (action.current = null)
        if (actionComponent.current != null) return

        val costStatus = cost.invoke(source)
        if (costStatus == NotPaid) {
            println("${source.info()} can't pay skill cost for skill: $name")
            return
        }

        //todo non-skill actions?
        //todo non-entity actions?
        actionComponent.current = {
            val amount = effectAmount(source)
            effect.invoke(info, amount)

            send(EntityChangedEvent(source))
            if (target is Entity) {
                send(EntityChangedEvent(target))
            }
            send(EntityFinishUseSkillEvent(info, amount))
        }

        actionComponent.turnsLeft = turns.toDouble()
        send(EntityStartUseSkillEvent(info))
    }
}