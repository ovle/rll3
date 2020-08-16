package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.system.EventSystem

class SkillSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntityUseSkill> { onEntityUseSkillEvent(it.source, it.target, it.skillTemplate) }
    }

    private fun onEntityUseSkillEvent(source: Entity, target: Any?, skillTemplate: SkillTemplate) {
        println("$source use skill ${skillTemplate.name} on $target")
        skill(source, target, skillTemplate)
    }
}