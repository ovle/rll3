package com.ovle.rll3.model.ai.bt

import com.ovle.rll3.model.ai.bt.Systems.action
import com.ovle.rll3.model.ai.bt.Systems.ai
import com.ovle.rll3.model.ai.bt.Systems.move

val AI = TestEnvironment(
    systems = arrayOf(ai)
)

val AIAndMovement = TestEnvironment(
    systems = arrayOf(ai, move, action)
)