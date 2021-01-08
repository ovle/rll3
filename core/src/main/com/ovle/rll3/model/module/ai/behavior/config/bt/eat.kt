package com.ovle.rll3.model.module.ai.behavior.config.bt

import com.ovle.rll3.model.module.ai.behavior.BTTemplate
import com.ovle.rll3.model.module.ai.behavior.config.task.eatTask
import com.ovle.rll3.model.module.ai.behavior.config.task.findNearestEntityTask
import com.ovle.rll3.model.module.ai.behavior.config.task.moveTask
import com.ovle.rll3.model.module.ai.behavior.config.task.successTask
import com.ovle.rll3.model.module.ai.behavior.seq
import com.ovle.rll3.model.module.ai.behavior.task
import com.ovle.rll3.model.module.ai.behavior.tree
import com.ovle.rll3.model.module.task.EntityConditions.isFoodEntity

val eatBt = BTTemplate(
    name = "eat",
    bt = { _ ->
        tree {
            seq {
                val nearestResource = task("find nearest food", findNearestEntityTask(::isFoodEntity))
                task("move to food", moveTask(nearestResource))
                task("take food", successTask())    //todo
                task("move with food to table room", successTask()) //todo
                task("eat food", eatTask(nearestResource))
            }
        }
    }
)