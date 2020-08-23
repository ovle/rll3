package com.ovle.rll3.model.module.ai.action

import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.ovle.rll3.SuccessCondition
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.BaseBlackboard
import com.ovle.rll3.model.module.ai.BaseTask
import com.ovle.rll3.model.module.task.EntityConditions.isDead
import com.ovle.rll3.model.module.task.EntityConditions.isExists
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.module.task.asEntityTarget
import com.ovle.rll3.model.template.TemplatesRegistry

@OptIn(ExperimentalStdlibApi::class)
class UseSkillAction: BaseTask() {

    @TaskAttribute(required = true)
    lateinit var skillName: String

    override fun executeIntr(): Status {
        val isSucceeded = isSkillSucceeded(skillName)
        if (isSucceeded.invoke(owner, target)) return Status.SUCCEEDED

        val skillTemplate = TemplatesRegistry.skillTemplates[skillName]!!
        val targetEntity = (target as TaskTarget.EntityTarget).entity

        EventBus.send(Event.GameEvent.EntityUseSkill(owner, targetEntity, skillTemplate))
        return Status.RUNNING
    }

    private fun isSkillSucceeded(skillName: String): SuccessCondition =
        when (skillName) {
            "gather" -> { _, t -> !isExists(t.asEntityTarget().entity) }
            "attack" -> { _, t -> isDead(t.asEntityTarget().entity) }
            else -> { _, _ -> false }
        }

    override fun copyTo(otherTask: Task<BaseBlackboard>): Task<BaseBlackboard> {
        return super.copyTo(otherTask).apply {
            otherTask as UseSkillAction
            otherTask.skillName = skillName
        }
    }
}