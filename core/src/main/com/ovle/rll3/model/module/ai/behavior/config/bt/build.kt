package com.ovle.rll3.model.module.ai.behavior.config.bt

import com.ovle.rll3.model.module.ai.behavior.BTTemplate
import com.ovle.rll3.model.module.ai.behavior.config.task.*
import com.ovle.rll3.model.module.ai.behavior.seq
import com.ovle.rll3.model.module.ai.behavior.task
import com.ovle.rll3.model.module.ai.behavior.tree
import com.ovle.rll3.model.util.conditions.EntityConditions.isBuildMaterialEntity
import com.ovle.rll3.model.template.skill

val buildBt = BTTemplate(
    name = "build",
    bt = { initialTarget ->
        tree {
            seq {
                val material = task("find material", findNearestEntityTask(::isBuildMaterialEntity))
                task("move to material", moveTask(material))
                task("take material", takeTask(material))
                val initialTargetPosition = task("find path to target tile", findPositionNearTarget(initialTarget))
                task("move to target", moveTask(initialTargetPosition))
                task("build", useSkill(skill("build"), initialTarget, material))   //todo what to build?
            }
        }
    }
)