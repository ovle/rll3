package com.ovle.rll3.model.module.ai.behavior.config.bt

import com.ovle.rll3.model.module.ai.behavior.*
import com.ovle.rll3.model.module.ai.behavior.config.task.findPositionNearTarget
import com.ovle.rll3.model.module.ai.behavior.config.task.moveTask
import com.ovle.rll3.model.module.ai.behavior.config.task.useSkill
import com.ovle.rll3.model.template.skill


val attackBt = BTTemplate(
    name = "attack",
    bt = { initialTarget ->
        tree {
            seq {
                val attackPosition = task("find attack position", findPositionNearTarget(initialTarget))
                task("move to attack position", moveTask(attackPosition))
                task("gather", useSkill(skill("attack"), initialTarget))
            }
        }
    }
)
