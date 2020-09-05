package com.ovle.rll3.model.procedural.grid.processor.location.structure

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.TileArray
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.grid.LocationProcessor
import com.ovle.rll3.model.util.gridToTileArray

class StructureProcessor(private val params: StructureProcessorParams) : LocationProcessor {

    override fun process(locationInfo: LocationInfo, gameEngine: Engine) {
        val tiles = locationInfo.tiles
        val entities = mutableListOf<Entity>()

        repeat(params.number) {
            spawnStructure(tiles, locationInfo.random)
        }

        locationInfo.entities.plusAssign(entities)
    }

    private fun spawnStructure(tiles: TileArray, random: RandomParams) {
        val gridFactory = params.factory

        val structureGrid = gridFactory.get(random)
        val structureSize = params.size
        val spawnPoint = spawnPoint(random.kRandom, tiles, structureSize, params.tilePreFilter, 10)
        if (spawnPoint == null) {
            println("spawn failed: can't get spawnPoint")
            return
        }
        val structureTiles = gridToTileArray(structureGrid, params.tileMapper)

        tiles.setArray(spawnPoint, structureTiles, params.tilePostFilter)
    }
}
