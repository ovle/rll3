package com.ovle.rll3.model.util

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.gdx.math.pathfinding.cost
import com.ovle.rlUtil.gdx.math.pathfinding.heuristics
import com.ovle.rll3.model.module.core.entity.bodyObstacles
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.tile.tilePassType


fun path(from: GridPoint2, to: GridPoint2, location: LocationInfo): List<GridPoint2> = com.ovle.rlUtil.gdx.math.pathfinding.aStar.path(
    from,
    to,
    location.tiles,
    location.entities.bodyObstacles(),
    heuristicsFn = ::heuristics,
    costFn = ::cost,
    tilePassTypeFn = ::tilePassType
)