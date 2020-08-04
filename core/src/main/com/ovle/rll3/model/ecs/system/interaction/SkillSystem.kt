package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.system.EventSystem
import com.ovle.rll3.model.ecs.system.interaction.skill.SkillTemplate
import com.ovle.rll3.model.ecs.system.interaction.skill.skill

class SkillSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<Event.EntityUseSkill> { onEntityUseSkillEvent(it.entity, it.target, it.skillTemplate) }
    }

    private fun onEntityUseSkillEvent(entity: Entity, target: Any?, skillTemplate: SkillTemplate) {
        println("$entity use skill ${skillTemplate.name} on $target")
        skill(entity, target, skillTemplate)
    }
}