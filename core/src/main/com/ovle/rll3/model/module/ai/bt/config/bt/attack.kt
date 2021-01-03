package com.ovle.rll3.model.module.ai.bt.config.bt

import com.ovle.rll3.model.module.ai.bt.*
import com.ovle.rll3.model.module.ai.bt.config.task.findPositionNearTarget
import com.ovle.rll3.model.module.ai.bt.config.task.moveTask
import com.ovle.rll3.model.module.ai.bt.config.task.useSkill
import com.ovle.rll3.model.template.skill


val attackBt = BTTemplate(
    name = "attack",
    bt = { initialTarget ->
        tree {
            seq {
                val attackPosition = task("find attack position", findPositionNearTarget(initialTarget))
                task("move to attack position", moveTask(attackPosition))
                task("gather", useSkill(initialTarget, skill("attack")))
            }
        }
    }
)
