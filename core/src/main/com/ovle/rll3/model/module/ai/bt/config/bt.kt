package com.ovle.rll3.model.module.ai.bt.config

import com.ovle.rll3.model.module.ai.bt.*


val gather = BTInfo(
    name = "gather",
    bt = { initialTarget ->
        tree {
            seq {
                val initialTargetPosition = task("find path to target", findPositionNearTarget(initialTarget))
                task("move to target", moveTask(initialTargetPosition))
                val gatheredResource = task("gather", gather(initialTarget))
//            task("find gathered resource", findGatheredResource())
//            task("move to gathered resource", moveTask())
//            task("take the resource", takeTask())
//            task("find resource storage", findResourceStorage())
//            task("move to storage", moveTask())
//            task("drop the resource", dropTask())
            }
        }
    }
)