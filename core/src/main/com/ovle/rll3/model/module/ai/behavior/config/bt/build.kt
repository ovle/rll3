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

val buildBt = BTTemplate(
    name = "build",
    bt = { initialTarget ->
        tree {
            seq {
                task("find material", successTask())
                task("go take material", successTask())
                val initialTargetPosition = task("find path to target tile", findPositionNearTarget(initialTarget))
                task("move to target", moveTask(initialTargetPosition))
                task("build", useSkill(initialTarget, skill("build")))   //todo what to build?
            }
        }
    }
)