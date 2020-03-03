package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.cellular.CellularAutomataGenerator
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings.CelullarAutomataSettings

class CelullarAutomataGridFactory: GridFactory {

    companion object {
        const val wallMarker = 1.0f
        const val pitMarker = 0.5f
        const val emptyTileMarker = 0.0f
    }

    override fun get(size: Int, settings: LevelGenerationSettings): Grid {
        settings as CelullarAutomataSettings

        val wallGrid = Grid(size)

        val generator = CellularAutomataGenerator.getInstance()
        generator.apply {
            marker = wallMarker
            deathLimit = 2  //more will kill the walls
            birthLimit = 4
//            aliveChance = 0.6f
            setInitiate(false)
        }

        CellularAutomataGenerator.initiate(wallGrid, generator)
        init(wallGrid, size, wallMarker)
        generator.generate(wallGrid)
        connect(wallGrid, emptyTileMarker, wallMarker)

        val pitGrid = Grid(size)
        generator.apply {
            marker = pitMarker
            deathLimit = 3
            birthLimit = 6
        }
        CellularAutomataGenerator.initiate(pitGrid, generator)
        generator.generate(pitGrid)

        return merge(arrayOf(wallGrid), size, MergeType.LastNotEmpty)
//        return merge(arrayOf(wallGrid, pitGrid), size, MergeType.LastNotEmpty)
    }

    //todo
    enum class MergeType {
        FirstNotEmpty {
            override fun apply(grids: Array<Grid>, x: Int, y: Int): Float? =
                grids.map { it.get(x, y) }.find { it != emptyTileMarker }
        },
        LastNotEmpty {
            override fun apply(grids: Array<Grid>, x: Int, y: Int): Float? =
                grids.map { it.get(x, y) }.findLast { it != emptyTileMarker }
        };

        abstract fun apply(grids: Array<Grid>, x: Int, y: Int): Float?
    }

    private fun merge(grids: Array<Grid>, size: Int, mergeType: MergeType): Grid {
        val result = Grid(size)
        for (x in (0 until size)) {
            for (y in (0 until size)) {
                val value = mergeType.apply(grids, x, y) ?: continue
                result.set(x, y, value)
            }
        }
        return result
    }

    private fun init(grid: Grid, size: Int, marker: Float) {
        for (x in (0 until size)) {
            grid.set(x, 0, marker)
            grid.set(x, size - 1, marker)
        }
        for (y in (0 until size)) {
            grid.set(0, y, marker)
            grid.set(size - 1, y, marker)
        }
    }
}