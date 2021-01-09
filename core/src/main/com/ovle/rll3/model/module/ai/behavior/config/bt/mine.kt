package com.ovle.rll3.model.module.ai.behavior.config.bt

import com.ovle.rll3.model.module.ai.behavior.BTTemplate
import com.ovle.rll3.model.module.ai.behavior.config.task.successTask
import com.ovle.rll3.model.module.ai.behavior.task
import com.ovle.rll3.model.module.ai.behavior.tree

val mineBt = BTTemplate(
    name = "mine",
    bt = { initialTarget ->
        tree {
            task("todo", successTask())
        }
    }
)