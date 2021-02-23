package com.ovle.rll3.model.module.ai.behavior.config.bt

import com.ovle.rll3.model.module.ai.behavior.BTTemplate
import com.ovle.rll3.model.module.ai.behavior.config.task.*
import com.ovle.rll3.model.module.ai.behavior.seq
import com.ovle.rll3.model.module.ai.behavior.task
import com.ovle.rll3.model.module.ai.behavior.tree
import com.ovle.rll3.model.util.conditions.EntityConditions.isResourceEntity
import com.ovle.rll3.model.template.skill

val gatherBt = BTTemplate(
    name = "gather",
    bt = { initialTarget ->
        tree {
            seq {
                val gatherPosition = task("find path to target", findPositionNearTarget(initialTarget))
                task("move to target", moveTask(gatherPosition))
                task("gather", useSkill(skill("gather"), initialTarget))
                val nearestResource = task("find gathered resource", findEntityOnPositionTask(initialTarget, ::isResourceEntity))
                task("take the resource", takeTask(nearestResource))
                val storagePosition = task("find resource storage", findResourceStorageTask())
                task("move to storage", moveTask(storagePosition))
                task("drop the resource", dropTask())
            }
        }
    }
)