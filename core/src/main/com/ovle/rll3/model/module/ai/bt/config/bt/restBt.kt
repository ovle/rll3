package com.ovle.rll3.model.module.ai.bt.config.bt

import com.ovle.rll3.model.module.ai.bt.*
import com.ovle.rll3.model.module.ai.bt.config.task.findRandomNearbyPoint
import com.ovle.rll3.model.module.ai.bt.config.task.moveTask
import com.ovle.rll3.model.module.ai.bt.config.task.restTask

val restBt = BTTemplate(
    name = "rest",
    bt = { initialTarget ->
        tree {
            select(random = true) {
                task("rest", restTask())
//                task("rest", restTask())
//                seq {
//                    val randomPoint = task("find random point", findRandomNearbyPoint())
//                    task("move to random point", moveTask(randomPoint))
//                }
            }
        }
    }
)