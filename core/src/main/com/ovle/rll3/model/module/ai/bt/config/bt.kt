package com.ovle.rll3.model.module.ai.bt.config

import com.ovle.rll3.model.module.ai.bt.*


val gather = BTInfo(
    name = "gather",
    bt = tree(
        seq {
            task("find path to target", findPositionNearTarget())
            task("move to target", moveTask())
            task("gather", useSkill("gather"))
        }
    )
)