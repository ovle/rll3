package com.ovle.rll3.model.module.ai.bt.config.bt

import com.ovle.rll3.model.module.ai.bt.*
import com.ovle.rll3.model.module.ai.bt.config.task.failTask
import com.ovle.rll3.model.module.ai.bt.config.task.successTask


val testBt = BTInfo(
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
