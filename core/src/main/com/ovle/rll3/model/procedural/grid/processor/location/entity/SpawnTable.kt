package com.ovle.rll3.model.procedural.grid.processor.location.entity

import com.ovle.rlUtil.*
import com.ovle.rll3.model.procedural.config.location.whateverTileId
import com.ovle.rll3.model.template.entity.SpawnTemplate
import com.ovle.rll3.model.template.entity.EntityTemplate
import kotlin.random.Random


class SpawnTable(spawnTemplates: List<EntityTemplate>, adjTiles: AdjTiles, val r: Random) {

    private val spawnData = spawnTemplates
        .map { it to it.spawns.filter { spawn -> matchesTemplate(spawn, adjTiles) } }
        .filter { (_, spawns) -> spawns.isNotEmpty() }


    @OptIn(ExperimentalStdlibApi::class)
    fun spawn(check: Double): EntityTemplate? {
        if (spawnData.isEmpty()) return null

        val spawns = spawnData.flatMap { it.second }
        val chanceTotal = spawns.size
//        val chanceTotalPositive = spawns.sumByDouble { it.chance.toDouble() }
        val coeff = 1.0 / chanceTotal.toDouble()    //todo high spawn chance for particular template
                                                    // can be very decreased by number of templates this way
        val table = spawns.map { it.chance * coeff }

        var value = check
        var spawnIndex = 0
        for (chance in table) {
            value -= chance
            if (value < 0) break

            spawnIndex++
        }
        if (spawnIndex >= spawnData.size) return null

        return spawnData[spawnIndex].first
    }

    private fun matchesTemplate(spawnTemplate: SpawnTemplate, adjTiles: AdjTiles): Boolean {
        val parsedMask = spawnTemplate.parsedMask ?: return true
        if (!spawnTemplate.rotate) {
            val maskTiles = parsedMask.reversed().flatten()
            return matchesMask(maskTiles, adjTiles)
        }

        val maskTilesR = rotate180(parsedMask).flatten()
        val maskTilesR1 = rotate90(parsedMask).flatten()
        val maskTilesR2 = parsedMask.flatten()
        val maskTilesR3 = rotate270(parsedMask).flatten()

        return matchesMask(maskTilesR, adjTiles) ||
            matchesMask(maskTilesR1, adjTiles) ||
            matchesMask(maskTilesR2, adjTiles) ||
            matchesMask(maskTilesR3, adjTiles)
    }

    private fun matchesMask(maskTiles: List<Tile>, adjTiles: AdjTiles): Boolean {
        val actualTiles = adjTiles.asList.flatten().map { it }
        return maskTiles
            .zip(actualTiles)
            .all { (maskTile, actualTile) -> maskTile == whateverTileId || maskTile == actualTile }
    }
}