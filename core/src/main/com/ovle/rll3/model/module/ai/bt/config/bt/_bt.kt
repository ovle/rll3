package com.ovle.rll3.model.module.ai.bt.config.bt

import com.ovle.rll3.model.module.ai.bt.*
import com.ovle.rll3.model.module.ai.bt.config.task.*
import com.ovle.rll3.model.module.gathering.ResourceType
import com.ovle.rll3.model.module.gathering.ResourceType.*


val eatBt = BTTemplate(
    name = "eat",
    bt = { _ ->
        tree {
            seq {
                val nearestResource = task("find nearest food", findNearestResourceTask(Food))
                task("move to food", moveTask(nearestResource))
                task("take food", successTask())    //todo
                task("move with food to table room", successTask()) //todo
                task("eat food", eatTask(nearestResource))
            }
        }
    }
)

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

val restBt = BTTemplate(
    name = "rest",
    bt = { initialTarget ->
        tree {
            select(random = true) {
                task("rest", restTask())
                task("rest", restTask())
                seq {
                    val randomPoint = task("find random point", findRandomNearbyPoint())
                    task("move to hideout", moveTask(randomPoint))
                }
            }
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
