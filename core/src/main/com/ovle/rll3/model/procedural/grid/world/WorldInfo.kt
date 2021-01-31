package com.ovle.rll3.model.procedural.grid.world

import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.WorldId
import com.ovle.rlUtil.RandomParams
import com.ovle.rlUtil.TileArray
import com.ovle.rlUtil.gdx.math.Area
import java.io.Serializable

data class WorldArea(
    val name: String,
    val area: Area
)

data class WorldInfo(
    val id: WorldId,
    var name: String,
    val random: RandomParams,
//    val params: WorldGenerationParams,
    val tiles: TileArray,
    val heightGrid: Grid,
    val heatGrid: Grid,
    val areas: Collection<WorldArea>
): Serializable {

    fun area(point: GridPoint2) = areas.first { point in it.area.points }
}