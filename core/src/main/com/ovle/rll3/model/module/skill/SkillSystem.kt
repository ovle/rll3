package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event.GameEvent.EntityEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.system.EventSystem

class SkillSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntityUseSkill> { onEntityUseSkillEvent(it.entity, it.target, it.skillTemplate) }
    }

    private fun onEntityUseSkillEvent(entity: Entity, target: Any?, skillTemplate: SkillTemplate) {
        println("$entity use skill ${skillTemplate.name} on $target")
        skill(entity, target, skillTemplate)
    }
}