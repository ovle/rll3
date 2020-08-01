package com.ovle.rll3.model.ecs.system.interaction.skill

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.ecs.entity.on
import com.ovle.rll3.model.tile.isPassable


fun entityTarget(position: GridPoint2, level: LevelInfo, check: (Entity) -> Boolean)
    = level.entities.on(position).singleOrNull { check(it) }

fun emptyTileTarget(position: GridPoint2, level: LevelInfo) = if (level.tiles.isPassable(position)) position else null
