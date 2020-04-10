package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.special.LevelDescription
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.Mappers.template
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

        //todo not spawn on level connection!

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

                val haveSameEntityNear = haveSameEntityNear(spawnTemplate.name, x, y, spawnCondition.groupRadius, entities)
                var chance = spawnCondition.chance
                if (haveSameEntityNear) chance += spawnCondition.groupBonusChance
                val needSpawn = check <= chance

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