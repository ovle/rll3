package com.ovle.rll3.model.module.ai.bt.config.task

import com.badlogic.gdx.ai.btree.Task.Status.RUNNING
import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rll3.TaskExec
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.bt.TaskTargetHolder
import com.ovle.rll3.model.module.ai.bt.result
import com.ovle.rll3.model.module.skill.SkillTemplate
import com.ovle.rll3.model.module.task.TaskTarget

fun useSkill(targetHolder: TaskTargetHolder, skill: SkillTemplate): TaskExec = { (btParams) ->
    val owner = btParams.owner
    val target = targetHolder.target
    target as TaskTarget

    val isSucceeded = skill.isSuccess.invoke(owner, target, btParams.location)

    if (isSucceeded) {
        result(SUCCEEDED)
    } else {
        val targetEntity = target.target
        EventBus.send(Event.GameEvent.EntityUseSkillCommand(owner, targetEntity, skill))

        result(RUNNING)
    }
}