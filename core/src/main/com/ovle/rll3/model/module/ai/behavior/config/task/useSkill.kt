package com.ovle.rll3.model.module.ai.behavior.config.task

import com.badlogic.gdx.ai.btree.Task.Status.RUNNING
import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rll3.TaskExec
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.Event.GameEvent.EntityUseSkillCommand
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.ai.behavior.TaskTargetHolder
import com.ovle.rll3.model.module.ai.behavior.result
import com.ovle.rll3.model.module.skill.SkillTemplate
import com.ovle.rll3.model.module.task.EntityConditions.isExists
import com.ovle.rll3.model.module.task.InvalidTargetException
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.module.task.checkValid
import com.ovle.rll3.model.module.task.isValidTarget

fun useSkill(targetHolder: TaskTargetHolder, skill: SkillTemplate): TaskExec = { (btParams) ->
    val owner = btParams.owner
    var target = targetHolder.target(validated = false)

    val isSucceeded = skill.isSuccess.invoke(owner, target, btParams.location)

    if (isSucceeded) {
        result(SUCCEEDED)
    } else {
        target = targetHolder.target(validated = true)

        val targetEntity = target.target
        send(EntityUseSkillCommand(owner, targetEntity, skill))

        result(RUNNING)
    }
}
