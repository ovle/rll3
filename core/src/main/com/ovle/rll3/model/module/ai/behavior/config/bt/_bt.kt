package com.ovle.rll3.model.module.ai.behavior.config.bt

import com.ovle.rll3.model.module.ai.behavior.*
import com.ovle.rll3.model.module.ai.behavior.config.task.*


val huntBt = BTTemplate(
    name = "hunt",
    bt = { initialTarget ->
        tree {
            task("todo", successTask())
        }
    }
)

val runawayBt = BTTemplate(
    name = "runaway",
    bt = { _ ->
        tree {
            seq {
                val nearestHideout = task("find nearest hideout", findNearestHideout())
                task("move to hideout", moveTask(nearestHideout))
            }
        }
    }
)

val combatBt = BTTemplate(
    name = "combat",
    bt = { initialTarget ->
        tree {
            task("todo", successTask())
        }
    }
)

val startCombatBt = BTTemplate(
    name = "start combat",
    bt = { initialTarget ->
        tree {
            task("todo", successTask())
        }
    }
)