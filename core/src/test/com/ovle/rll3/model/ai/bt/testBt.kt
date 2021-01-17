package com.ovle.rll3.model.module.ai.behavior.config.bt

import com.ovle.rll3.model.module.ai.behavior.*
import com.ovle.rll3.model.module.ai.behavior.config.task.*
import com.ovle.rll3.model.module.task.EntityConditions
import com.ovle.rll3.model.module.task.EntityConditions.isResourceEntity
import com.ovle.rll3.model.template.skill

val testEntityValidationBt = BTTemplate(
    name = "testEntityValidationBt",
    bt = { initialTarget ->
        tree {
            seq {
                val initialTargetPosition = task("find path to target", findPositionNearTarget(initialTarget))
                task("move to target", moveTask(initialTargetPosition))
                task("gather", useSkill(initialTarget, skill("gather")))

                task("move to gathered resource", moveTask(initialTarget))
                task("take the resource", takeTask(initialTarget))

                val storagePosition = task("find resource storage", findResourceStorageTask())
                task("move to storage", moveTask(storagePosition))
                task("drop the resource", dropTask())

                task("move to target again", moveTask(initialTargetPosition))
                task("gather again", useSkill(initialTarget, skill("gather")))
            }
        }
    }
)

val testBt = BTTemplate(
    name = "testBt",
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
