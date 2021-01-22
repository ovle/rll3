package com.ovle.rll3.model.ai.bt

import com.ovle.rll3.model.ai.bt.Components.action
import com.ovle.rll3.model.ai.bt.Components.ai
import com.ovle.rll3.model.ai.bt.Components.move
import com.ovle.rll3.model.ai.bt.Components.position
import com.ovle.rll3.model.ai.bt.Systems.action
import com.ovle.rll3.model.ai.bt.Systems.ai
import com.ovle.rll3.model.ai.bt.Systems.move

val AI = TestEnvironment(
    systems = arrayOf(ai),
    entities = arrayOf(
        ent("aiOwner", ai())
    )
)

val AIAndMovement = TestEnvironment(
    systems = arrayOf(ai, move, action),
    entities = arrayOf(
        ent("aiOwner", ai(), move(), position(), action())
    )
)