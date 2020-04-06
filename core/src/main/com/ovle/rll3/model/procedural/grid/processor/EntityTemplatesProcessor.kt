package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.special.LevelDescription
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.ecs.entity.newTemplatedEntity
import com.ovle.rll3.model.template.EntityTemplates
import com.ovle.rll3.model.template.SpawnTemplate
import com.ovle.rll3.model.tile.*
import com.ovle.rll3.model.util.rotate180
import com.ovle.rll3.model.util.rotate270
import com.ovle.rll3.model.util.rotate90
import com.ovle.rll3.point
import com.ovle.rll3.random
import ktx.ashley.get

//todo templates may be different?
class EntityTemplatesProcessor(val templates: EntityTemplates) : TilesProcessor {

    override fun process(levelInfo: LevelInfo, gameEngine: Engine, levelDescription: LevelDescription) {
        val tiles = levelInfo.tiles
        val entities = mutableListOf<Entity>()
        val spawnTemplates = templates.templates.filter { it.spawns.isNotEmpty() }

        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
                val nearTiles = nearValues(tiles, x, y)
                val spawnDatas = spawnTemplates
                    .map { it to it.spawns.filter { spawn -> matchesTemplate(spawn, nearTiles) } }
                    .filter { (_, spawns) -> spawns.isNotEmpty() }

                //todo spawn table
                val spawnData = spawnDatas.random() ?: continue
                val spawnTemplate = spawnData.first
                val spawnCondition = spawnData.second.random()!!
                val check = Math.random()
                val needSpawn = check <= spawnCondition.chance

                if (needSpawn) {
                    val spawnPosition = point(x, y)
                    val entity = newTemplatedEntity(spawnTemplate, gameEngine)
                    entity[position]?.position = floatPoint(spawnPosition)
                    entities.add(entity)
                }
            }
        }

        levelInfo.objects.plusAssign(entities)
    }

    private fun matchesTemplate(spawnTemplate: SpawnTemplate, nearTiles: NearValues<Tile?>): Boolean {
        val parsedMask = spawnTemplate.parsedMask ?: return true
        if (!spawnTemplate.rotate) {
            val maskTiles = parsedMask.reversed().flatten()
            return matchesMask(maskTiles, nearTiles)
        }

        val maskTilesR = rotate180(parsedMask).flatten()
        val maskTilesR1 = rotate90(parsedMask).flatten()
        val maskTilesR2 = parsedMask.flatten()
        val maskTilesR3 = rotate270(parsedMask).flatten()

        return matchesMask(maskTilesR, nearTiles) ||
            matchesMask(maskTilesR1, nearTiles) ||
            matchesMask(maskTilesR2, nearTiles) ||
            matchesMask(maskTilesR3, nearTiles)
    }

    private fun matchesMask(maskTiles: List<TileType>, nearTiles: NearValues<Tile?>): Boolean {
        val actualTiles = nearTiles.asList.flatten().map { it?.typeId }
        return maskTiles
            .zip(actualTiles)
            .all { (maskTile, actualTile) -> maskTile == whateverTileId || maskTile == actualTile }
    }
}