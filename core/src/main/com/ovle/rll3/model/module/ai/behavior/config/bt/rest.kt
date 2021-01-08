package com.ovle.rll3.model.module.ai.behavior.config.bt

import com.ovle.rll3.model.module.ai.behavior.*
import com.ovle.rll3.model.module.ai.behavior.config.task.restTask

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