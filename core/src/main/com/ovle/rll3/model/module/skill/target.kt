package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.core.entity.on
import com.ovle.rll3.model.tile.isPassable


fun entityTarget(position: GridPoint2, location: LocationInfo, check: (Entity) -> Boolean)
    = location.entities.on(position).singleOrNull { check(it) }

fun emptyTileTarget(position: GridPoint2, location: LocationInfo) = if (location.tiles.isPassable(position)) position else null
