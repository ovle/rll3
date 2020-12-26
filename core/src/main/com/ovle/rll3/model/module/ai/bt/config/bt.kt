package com.ovle.rll3.model.module.ai.bt.config

import com.ovle.rll3.model.module.ai.bt.*


val gather = BTInfo(
    name = "gather",
    bt = { initialTarget ->
        tree {
            seq {
                val initialTargetPosition = task("find path to target", findPositionNearTarget(initialTarget))
                task("move to target", moveTask(initialTargetPosition))
                task("gather", gather(initialTarget))
                val nearestResource = task("find nearest resource", findNearestResource())
                task("move to gathered resource", moveTask(nearestResource))
                task("take the resource", takeTask(nearestResource))
                val storagePosition = task("find resource storage", findResourceStorage())
                task("move to storage", moveTask(storagePosition))
                task("drop the resource", dropTask())
            }
        }
    }
)