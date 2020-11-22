package com.ovle.rll3.model.module.ai.action

import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.ovle.rll3.event.Event.GameEvent.EntityUseSkillCommand
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.BaseBlackboard
import com.ovle.rll3.model.module.ai.BaseTask
import com.ovle.rll3.model.module.core.entity.locationInfo
import com.ovle.rll3.model.template.TemplatesRegistry

//todo is bt needed at all at this point, with such actions?
@OptIn(ExperimentalStdlibApi::class)
class UseSkillAction: BaseTask() {

    @TaskAttribute(required = true)
    lateinit var skillName: String

    override fun executeIntr(): Status {
        val skillTemplate = TemplatesRegistry.skillTemplates[skillName]!!
        val isSucceeded = skillTemplate.isSuccess
        if (isSucceeded.invoke(owner, target, locationInfo())) return Status.SUCCEEDED

        val targetEntity = target.unbox()
        EventBus.send(EntityUseSkillCommand(owner, targetEntity, skillTemplate))
        return Status.RUNNING
    }

    private fun locationInfo() = locationInfo(entities.toTypedArray())!!

    override fun copyTo(otherTask: Task<BaseBlackboard>): Task<BaseBlackboard> {
        return super.copyTo(otherTask).apply {
            otherTask as UseSkillAction
            otherTask.skillName = skillName
        }
    }
}