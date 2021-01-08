package com.ovle.rll3.model.module.ai.behavior.config.bt

import com.ovle.rll3.model.module.ai.behavior.*
import com.ovle.rll3.model.module.ai.behavior.config.task.*


val huntBt = BTTemplate(
    name = "hunt",
    bt = { initialTarget ->
        tree {

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

        }
    }
)

val startCombatBt = BTTemplate(
    name = "runaway",
    bt = { initialTarget ->
        tree {

        }
    }
)

val testBt = BTTemplate(
    name = "test",
    bt = { initialTarget ->
        tree {
            seq {
                select {
                    task("branch 0", failTask())
                    seq {
                        task("branch 1.1", failTask())
                        seq {
                            task("branch 1.2.1", successTask())
                            task("branch 1.2.2", successTask())
                        }
                    }
                    seq {
                        task("branch 2.1", successTask())
                        task("branch 2.2", successTask())
                    }
                    task("branch 3", successTask())
                }
                task("branch 4", successTask())
            }
        }
    }
)
