package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings

//todo
class TemplateGridFactory: GridFactory {
    override fun get(size: Int, settings: LevelGenerationSettings): Grid {
        settings as LevelGenerationSettings.TemplateGenerationSettings

        val grid = Grid(size)
        return grid
    }

    //private fun tileIndexToTile(index: Int, size: Int) = Tile(typeId = template[index % size][index / size])
}