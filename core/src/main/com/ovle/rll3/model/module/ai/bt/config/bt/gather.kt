package com.ovle.rll3.model.module.ai.bt.config.bt

import com.ovle.rll3.model.module.ai.bt.BTInfo
import com.ovle.rll3.model.module.ai.bt.config.task.*
import com.ovle.rll3.model.module.ai.bt.seq
import com.ovle.rll3.model.module.ai.bt.task
import com.ovle.rll3.model.module.ai.bt.tree
import com.ovle.rll3.model.template.skill

val gatherBt = BTInfo(
    name = "gather",
    bt = { initialTarget ->
        tree {
            seq {
                val initialTargetPosition = task("find path to target", findPositionNearTarget(initialTarget))
                task("move to target", moveTask(initialTargetPosition))
                task("gather", useSkill(initialTarget, skill("gather")))
                val nearestResource = task("find nearest resource", findNearestResourceTask())
                task("move to gathered resource", moveTask(nearestResource))
                task("take the resource", takeTask(nearestResource))
                val storagePosition = task("find resource storage", findResourceStorageTask())
                task("move to storage", moveTask(storagePosition))
                task("drop the resource", dropTask())
            }
        }
    }
)