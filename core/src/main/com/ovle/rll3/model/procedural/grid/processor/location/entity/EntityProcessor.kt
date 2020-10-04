package com.ovle.rll3.model.procedural.grid.processor.location.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.assets.loader.EntityTemplates
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.entity.positions
import com.ovle.rll3.model.module.core.entity.newTemplatedEntity
import com.ovle.rll3.model.module.core.entity.randomId
import com.ovle.rll3.model.procedural.grid.LocationProcessor
import com.ovle.rll3.model.tile.*
import com.ovle.rll3.point
import ktx.ashley.get

class EntityProcessor(val templates: EntityTemplates) : LocationProcessor {

    @OptIn(ExperimentalStdlibApi::class)
    override fun process(locationInfo: LocationInfo, gameEngine: Engine) {
        val tiles = locationInfo.tiles
        //some cells can be claimed by other processors
        val claimed = locationInfo.entities.positions()
        val entities = mutableListOf<Entity>()
        val spawnTemplates = templates.templates.filter { it.spawns.isNotEmpty() }

        val random = locationInfo.random.kRandom
        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
                if (point(x, y) in claimed) continue

                val nearTiles = nearValues(tiles, x, y)
                val spawnTable = SpawnTable(spawnTemplates, nearTiles, random)
                val check = random.nextDouble()
                val spawnTemplate = spawnTable.spawn(check) ?: continue

//                val spawnCondition = spawnData.second
//                val haveSameEntityNear = haveSameEntityNear(spawnTemplate.name, x, y, spawnCondition.groupRadius, entities)
//                var chance = spawnCondition.chance
//                if (haveSameEntityNear) chance += spawnCondition.groupBonusChance
//                val needSpawn = check <= chance

                val spawnPosition = point(x, y)
                val id = randomId()
                val entity = newTemplatedEntity(id, spawnTemplate, gameEngine)
                entity[position]?.gridPosition = spawnPosition
                entities.add(entity)
            }
        }

        locationInfo.entities.plusAssign(entities)
    }

//    private fun haveSameEntityNear(templateName: String, x: Int, y: Int, radius: Int, entities: MutableList<Entity>): Boolean {
//        val sameEntities = entities.filter { it[template]?.template?.name == templateName }
//        val nearRangeX = (x - radius)..(x + radius)
//        val nearRangeY = (y - radius)..(y + radius)
//        return sameEntities.map { it[position]!!.gridPosition }.any { it.x in nearRangeX && it.y in nearRangeY }
//    }
}