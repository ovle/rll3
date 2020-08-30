package com.ovle.rll3.screen.game

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.procedural.grid.world.WorldInfo

data class InitGameInfo(val world: WorldInfo, val locationPoint: GridPoint2)