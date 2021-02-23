package com.ovle.rll3.model.ai.bt

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.TileArray
import com.ovle.rll3.model.module.ai.AIComponent
import com.ovle.rll3.model.module.ai.AISystem
import com.ovle.rll3.model.module.core.entity.randomId
import com.ovle.rll3.model.module.entityAction.EntityActionComponent
import com.ovle.rll3.model.module.entityAction.EntityActionSystem
import com.ovle.rll3.model.module.game.dto.LocationInfo
import com.ovle.rll3.model.module.space.MoveComponent
import com.ovle.rll3.model.module.space.MoveSystem
import com.ovle.rll3.model.module.space.PositionComponent
import com.ovle.rlUtil.RandomParams
import com.ovle.rll3.model.procedural.config.location.lowGroundTileId
import com.ovle.rll3.model.procedural.grid.world.WorldInfo
import com.ovle.rlUtil.gdx.math.point
import kotlin.math.sqrt

const val empty = lowGroundTileId

val tiles = arrayOf(
    empty, empty, empty,
    empty, empty, empty,
    empty, empty, empty
)

val random = RandomParams(0L)
val worldInfo = WorldInfo(randomId(), "", random, TileArray(arrayOf(), 0), Grid(0), Grid(0), listOf())
val locationInfo = LocationInfo(randomId(), point(0, 0), random, TileArray(tiles, sqrt(tiles.size.toDouble()).toInt()))


object Systems {
    val ai = AISystem(isRealTime = true)
    val move = MoveSystem()
    val action = EntityActionSystem()
}

object Components {
    fun ai() = AIComponent("base")
    fun move() = MoveComponent()
    fun position() = PositionComponent()
    fun action() = EntityActionComponent()
}