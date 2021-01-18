package com.ovle.rll3.model.module.ai.behavior.config.bt

import com.ovle.rll3.model.module.ai.behavior.BTTemplate
import com.ovle.rll3.model.module.ai.behavior.config.task.findPositionNearTarget
import com.ovle.rll3.model.module.ai.behavior.config.task.moveTask
import com.ovle.rll3.model.module.ai.behavior.config.task.successTask
import com.ovle.rll3.model.module.ai.behavior.config.task.useSkill
import com.ovle.rll3.model.module.ai.behavior.seq
import com.ovle.rll3.model.module.ai.behavior.task
import com.ovle.rll3.model.module.ai.behavior.tree
import com.ovle.rll3.model.template.skill

val mineBt = BTTemplate(
    name = "mine",
    bt = { initialTarget ->
        tree {
            seq {
                val minePosition = task("find path to mine target", findPositionNearTarget(initialTarget))
                task("move to target", moveTask(minePosition))
                task("mine", useSkill(initialTarget, skill("mine")))
                //todo remove mined stuff if exists?
            }
        }
    }
)