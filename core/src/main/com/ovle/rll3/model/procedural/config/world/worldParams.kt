package com.ovle.rll3.model.procedural.config.world

import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rlUtil.noise4j.grid.factory.impl.FractalGridFactory
import com.ovle.rlUtil.noise4j.grid.factory.impl.FractalGridFactoryParams
import com.ovle.rll3.model.procedural.config.WorldGenerationParams
import com.ovle.rll3.model.procedural.config.location.outdoorLowWallTreshold
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
    heatMapFactory = FractalGridFactory(
        params = FractalGridFactoryParams(
            size = worldSize,
            startIteration = 1,
            flexibleNoiseValue = 1.0f,
            initialBorderValues = arrayOf(
                FloatArray(3, heatGridInitializer()),
                FloatArray(3, heatGridInitializer()),
                FloatArray(3, heatGridInitializer())
            )
        )
    ),
//    heatMapFactory = Combine(
//        factory1 = GradientGridFactory(
//            params = GradientGridFactoryParams(
//                size = worldSize,
//                isHorisontal = true
//            )
//        ),
//        factory2 = CelullarAutomataGridFactory(
//            params = CelullarAutomataGridFactoryParams(
//                size = worldSize,
//                iterationsAmount = 12,
//                radius = 1,
//                deathLimit = 2,
//                birthLimit = 2,
//                aliveChance = 0.1f
//            )
//        ),
//        combinator = ::heatGridValueCombinator
//    ),
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

private fun heatGridInitializer() = { i: Int -> if (i == 1) 0.9f else 0.0f }

private fun isRiverStartPoint(grid1: Grid, grid2: Grid, point: GridPoint2): Boolean {
    val heightValue = grid1[point.x, point.y]
    val heatValue = grid2[point.x, point.y]

    return heightValue >= outdoorLowWallTreshold && heatValue <= heatDesertTreshold
}