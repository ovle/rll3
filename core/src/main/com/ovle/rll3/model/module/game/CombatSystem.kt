package com.ovle.rll3.model.module.game

import com.ovle.rll3.event.Event.ApplyTurnCommand
import com.ovle.rll3.event.Event.TurnAppliedEvent
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.action
import com.ovle.rll3.model.module.core.entity.actionEntities
import com.ovle.rll3.model.module.core.system.EventSystem
import ktx.ashley.get


class CombatSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<ApplyTurnCommand> { onApplyTurnCommand() }
    }

    private fun onApplyTurnCommand() {
        val actionEntities = actionEntities()
        actionEntities.forEach {
            val action = it[action]!!
            with (action) {
                val skill = selectedSkill ?: return@with

                val effectAmount = skill.skillEffectAmount.invoke(it)
                skill.cost.invoke(it)
                skill.skillEffect.invoke(it, selectedSkillTarget, effectAmount)
            }
        }

        EventBus.send(TurnAppliedEvent())
    }
}