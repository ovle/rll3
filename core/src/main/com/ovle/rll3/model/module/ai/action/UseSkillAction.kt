package com.ovle.rll3.model.module.ai.action

import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.BaseTask
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.template.TemplatesRegistry

@OptIn(ExperimentalStdlibApi::class)
class UseSkillAction: BaseTask() {

    @TaskAttribute(required = true)
    lateinit var skillName: String

    override fun executeIntr(): Status {
        val skillTemplate = TemplatesRegistry.skillTemplates[skillName]!!
        val targetEntity = (target as TaskTarget.EntityTarget).entity
        EventBus.send(Event.GameEvent.EntityUseSkill(owner, targetEntity, skillTemplate))

        return Status.SUCCEEDED
    }
}