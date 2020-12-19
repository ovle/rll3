package com.ovle.rll3.model.module.ai.bt.config

import com.ovle.rll3.model.module.ai.bt.*


val testTreeInfo = BTInfo(
    name = "test",
    bt = tree(
        seq {
            task(findPositionNearTarget())
            task(moveTask())
            task(useSkill("gather"))

            this
        }
    )
)