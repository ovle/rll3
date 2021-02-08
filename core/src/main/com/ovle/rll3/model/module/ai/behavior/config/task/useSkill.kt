package com.ovle.rll3.model.module.ai.behavior.config.task

import com.badlogic.gdx.ai.btree.Task.Status.RUNNING
import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rll3.TaskExec
import com.ovle.rll3.event.EntityUseSkillCommand
import com.ovle.rll3.model.module.ai.behavior.TaskTargetHolder
import com.ovle.rll3.model.module.ai.behavior.result
import com.ovle.rll3.model.module.skill.SkillTemplate
import com.ovle.rll3.model.module.skill.SkillUsage

fun useSkill(skill: SkillTemplate, targetHolder: TaskTargetHolder, payloadHolder: TaskTargetHolder? = null): TaskExec = { (btParams) ->
    val owner = btParams.owner
    var target = targetHolder.target(validated = false)

    val isSucceeded = skill.isSuccess.invoke(owner, target, btParams.location)

    if (isSucceeded) {
        result(SUCCEEDED)
    } else {
        target = targetHolder.target(validated = true)
        val payload = payloadHolder?.target?.target

        val usage = SkillUsage(skill, owner, target.target, payload)
        send(EntityUseSkillCommand(usage))

        result(RUNNING)
    }
}
