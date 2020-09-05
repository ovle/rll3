package com.ovle.rll3.model.procedural.config.world

import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.procedural.config.GridFactoryParams.*
import com.ovle.rll3.model.procedural.config.WorldGenerationParams
import com.ovle.rll3.model.procedural.config.location.outdoorLowWallTreshold
import com.ovle.rll3.model.procedural.grid.factory.CelullarAutomataGridFactory
import com.ovle.rll3.model.procedural.grid.factory.FractalGridFactory
import com.ovle.rll3.model.procedural.grid.factory.GradientGridFactory
import com.ovle.rll3.model.procedural.grid.factory.Combine
import com.ovle.rll3.model.procedural.grid.processor.world.RiverWorldProcessor
import com.ovle.rll3.model.procedural.grid.processor.world.RiverWorldProcessorParams

const val worldSize = 257

val worldParams = WorldGenerationParams(
    templateName = "Common",
    heightMapFactory = FractalGridFactory(
        params = FractalGridFactoryParams(
            size = worldSize,
            startIteration = 3,
            flexibleNoiseValue = 3.0f
        )
    ),
    heatMapFactory = Combine(
        factory1 = GradientGridFactory(
            params = GradientGridFactoryParams(
                size = worldSize,
                isHorisontal = true
            )
        ),
        factory2 = CelullarAutomataGridFactory(
            params = CelullarAutomataGridFactoryParams(
                size = worldSize,
                iterationsAmount = 12,
                radius = 1,
                deathLimit = 2,
                birthLimit = 2,
                aliveChance = 0.1f
            )
        ),
        combinator = ::heatGridValueCombinator
    ),
    postProcessors = arrayOf(
        RiverWorldProcessor(
            params = RiverWorldProcessorParams(
                count = 20,
                erosionPower = 0.02f,
                isStartPoint = ::isRiverStartPoint
            )
        )
    ),
    tileMapper = ::tileMapper
)


private fun isRiverStartPoint(grid1: Grid, grid2: Grid, point: GridPoint2): Boolean {
    val heightValue = grid1[point.x, point.y]
    val heatValue = grid2[point.x, point.y]

    return heightValue >= outdoorLowWallTreshold && heatValue <= heatUpperTreshold
}