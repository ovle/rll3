package com.ovle.rll3.model.module.game

import com.ovle.rlUtil.gdx.ashley.component.BaseComponent
import com.ovle.rll3.model.module.time.TimeInfo
import com.ovle.rll3.model.procedural.grid.world.WorldInfo

class GameComponent(
    val location: LocationInfo,
    val world: WorldInfo?,
    val time: TimeInfo
): BaseComponent