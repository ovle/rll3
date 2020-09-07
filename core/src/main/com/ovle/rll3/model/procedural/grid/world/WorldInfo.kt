package com.ovle.rll3.model.procedural.grid.world

import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.TileArray
import com.ovle.rll3.WorldId
import com.ovle.rll3.model.module.time.TimeInfo
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.config.WorldGenerationParams
import com.ovle.rll3.model.util.Area
import java.io.Serializable

data class WorldArea(
    val name: String,
    val area: Area
)

data class WorldInfo(
    val id: WorldId,
    var name: String,
    val random: RandomParams,
    val params: WorldGenerationParams,
    val tiles: TileArray,
    val heightGrid: Grid,
    val heatGrid: Grid,
    val areas: Collection<WorldArea>
): Serializable {

    fun area(point: GridPoint2) = areas.first { point in it.area.points }
}