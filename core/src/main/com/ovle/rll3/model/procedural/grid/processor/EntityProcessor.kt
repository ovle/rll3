package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.ecs.component.special.WorldInfo
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.ecs.entity.positions
import com.ovle.rll3.model.ecs.entity.newTemplatedEntity
import com.ovle.rll3.model.ecs.entity.randomId
import com.ovle.rll3.model.template.SpawnTemplate
import com.ovle.rll3.model.template.entity.EntityTemplates
import com.ovle.rll3.model.tile.*
import com.ovle.rll3.model.util.rotate180
import com.ovle.rll3.model.util.rotate270
import com.ovle.rll3.model.util.rotate90
import com.ovle.rll3.point
import ktx.ashley.get

class EntityProcessor(val templates: EntityTemplates) : TilesProcessor {

    @OptIn(ExperimentalStdlibApi::class)
    override fun process(levelInfo: LevelInfo, worldInfo: WorldInfo, gameEngine: Engine) {
        val tiles = levelInfo.tiles
        //some cells can be claimed by other processors
        val claimed = levelInfo.entities.positions()
        val r = worldInfo.r
        val entities = mutableListOf<Entity>()
        val spawnTemplates = templates.templates.filter { it.spawns.isNotEmpty() }

        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
                if (point(x, y) in claimed) continue

                val nearTiles = nearValues(tiles, x, y)
                val spawnDatas = spawnTemplates
                    .map { it to it.spawns.filter { spawn -> matchesTemplate(spawn, nearTiles) } }
                    .filter { (_, spawns) -> spawns.isNotEmpty() }

                //todo spawn table
                val spawnData = spawnDatas.randomOrNull(r) ?: continue
                val spawnTemplate = spawnData.first
                val spawnCondition = spawnData.second.random(r)
                val check = r.nextDouble()

                val haveSameEntityNear = haveSameEntityNear(spawnTemplate.name, x, y, spawnCondition.groupRadius, entities)
                var chance = spawnCondition.chance
                if (haveSameEntityNear) chance += spawnCondition.groupBonusChance
                val needSpawn = check <= chance

                if (needSpawn) {
                    val spawnPosition = point(x, y)
                    val id = randomId()
                    val entity = newTemplatedEntity(id, spawnTemplate, gameEngine)
                    entity[position]?.gridPosition = spawnPosition
                    entities.add(entity)
                }
            }
        }

        levelInfo.entities.plusAssign(entities)
    }

    private fun haveSameEntityNear(templateName: String, x: Int, y: Int, radius: Int, entities: MutableList<Entity>): Boolean {
        val sameEntities = entities.filter { it[template]?.template?.name == templateName }
        val nearRangeX = (x - radius)..(x + radius)
        val nearRangeY = (y - radius)..(y + radius)
        return sameEntities.map { it[position]!!.gridPosition }.any { it.x in nearRangeX && it.y in nearRangeY }
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