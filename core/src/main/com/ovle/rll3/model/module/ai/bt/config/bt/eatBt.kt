package com.ovle.rll3.model.module.ai.bt.config.bt

import com.ovle.rll3.model.module.ai.bt.BTTemplate
import com.ovle.rll3.model.module.ai.bt.config.task.eatTask
import com.ovle.rll3.model.module.ai.bt.config.task.findNearestResourceTask
import com.ovle.rll3.model.module.ai.bt.config.task.moveTask
import com.ovle.rll3.model.module.ai.bt.config.task.successTask
import com.ovle.rll3.model.module.ai.bt.seq
import com.ovle.rll3.model.module.ai.bt.task
import com.ovle.rll3.model.module.ai.bt.tree
import com.ovle.rll3.model.module.gathering.ResourceType

val eatBt = BTTemplate(
    name = "eat",
    bt = { _ ->
        tree {
            seq {
                val nearestResource = task("find nearest food", findNearestResourceTask(ResourceType.Food))
                task("move to food", moveTask(nearestResource))
                task("take food", successTask())    //todo
                task("move with food to table room", successTask()) //todo
                task("eat food", eatTask(nearestResource))
            }
        }
    }
)