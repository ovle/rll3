package com.ovle.rll3.model.module.game

import com.ovle.rll3.model.module.core.component.BaseComponent
import com.ovle.rll3.model.module.time.TurnInfo
import com.ovle.rll3.model.procedural.grid.world.WorldInfo

class GameComponent(
    val location: LocationInfo,
    val world: WorldInfo,
    val turn: TurnInfo,
    val party: PartyInfo
): BaseComponent