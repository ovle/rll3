package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.ecs.component.special.WorldInfo
import com.ovle.rll3.model.procedural.config.LevelFactoryParams

//todo
class TemplateGridFactory: GridFactory {
    override fun get(factoryParams: LevelFactoryParams, r: WorldInfo): Grid {
        factoryParams as LevelFactoryParams.TemplateLevelFactoryParams

        val grid = Grid(factoryParams.size)
        return grid
    }

    //private fun tileIndexToTile(index: Int, size: Int) = Tile(typeId = template[index % size][index / size])
}