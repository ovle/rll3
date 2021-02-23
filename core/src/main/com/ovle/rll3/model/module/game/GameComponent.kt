package com.ovle.rll3.model.module.game

import com.ovle.rll3.model.module.core.component.GlobalComponent
import com.ovle.rll3.model.module.game.dto.LocationInfo
import com.ovle.rll3.model.procedural.grid.world.WorldInfo

/**
 * game in the context of player
 */
class GameComponent(
    val location: LocationInfo,
    val world: WorldInfo?
): GlobalComponent()