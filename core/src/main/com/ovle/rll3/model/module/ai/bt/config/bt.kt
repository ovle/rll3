package com.ovle.rll3.model.module.ai.bt.config

import com.ovle.rll3.model.module.ai.bt.BTInfo
import com.ovle.rll3.model.module.ai.bt.seq
import com.ovle.rll3.model.module.ai.bt.task
import com.ovle.rll3.model.module.ai.bt.tree
import com.ovle.rll3.model.template.skill


val gather = BTInfo(
    name = "gather",
    bt = tree(
        seq {
            task("find path to target", findPositionNearTarget())
            task("move to target", moveTask())
            task("gather", useSkill(skill("gather")))
            task("find gathered resource", findGatheredResource())
            task("move to gathered resource", moveTask())
            task("take the resource", takeTask())
            task("find resource storage", findResourceStorage())
            task("move to storage", moveTask())
            task("drop the resource", dropTask())
        }
    )
)