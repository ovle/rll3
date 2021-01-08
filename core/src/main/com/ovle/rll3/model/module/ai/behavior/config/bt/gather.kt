package com.ovle.rll3.model.module.ai.behavior.config.bt

import com.ovle.rll3.model.module.ai.behavior.BTTemplate
import com.ovle.rll3.model.module.ai.behavior.config.task.*
import com.ovle.rll3.model.module.ai.behavior.seq
import com.ovle.rll3.model.module.ai.behavior.task
import com.ovle.rll3.model.module.ai.behavior.tree
import com.ovle.rll3.model.module.task.EntityConditions
import com.ovle.rll3.model.module.task.EntityConditions.isResourceEntity
import com.ovle.rll3.model.template.skill

val gatherBt = BTTemplate(
    name = "gather",
    bt = { initialTarget ->
        tree {
            seq {
                val initialTargetPosition = task("find path to target", findPositionNearTarget(initialTarget))
                task("move to target", moveTask(initialTargetPosition))
                task("gather", useSkill(initialTarget, skill("gather")))
                val nearestResource = task("find nearest resource", findNearestEntityTask(::isResourceEntity))
                task("move to gathered resource", moveTask(nearestResource))
                task("take the resource", takeTask(nearestResource))
                val storagePosition = task("find resource storage", findResourceStorageTask())
                task("move to storage", moveTask(storagePosition))
                task("drop the resource", dropTask())
            }
        }
    }
)