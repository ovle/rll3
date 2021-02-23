package com.ovle.rll3.model.procedural.grid.processor.location.structure

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.Tile
import com.ovle.rlUtil.TileArray
import com.ovle.rlUtil.gdx.math.component1
import com.ovle.rlUtil.gdx.math.component2
import com.ovle.rlUtil.gdx.math.point
import com.ovle.rll3.model.module.quest.QuestOwnerComponent
import com.ovle.rll3.model.module.game.dto.LocationInfo
import com.ovle.rll3.model.util.newEntity
import com.ovle.rll3.model.module.core.entity.newTemplatedEntity
import com.ovle.rll3.model.template.entity.entityTemplate
import com.ovle.rll3.model.template.structure.StructureEntity
import com.ovle.rll3.model.template.structure.StructureTemplate
import com.ovle.rll3.assets.loader.StructureTemplates
import com.ovle.rll3.model.module.quest.Components.questOwner
import com.ovle.rll3.model.module.space.Components.position
import com.ovle.rll3.model.procedural.config.location.groundTileFilter
import com.ovle.rll3.model.procedural.grid.LocationProcessor
import com.ovle.rll3.model.procedural.config.location.whateverTileId
import com.ovle.rll3.model.util.randomId
import ktx.ashley.get

data class StructureTemplateInfo(val template: StructureTemplate, val positions: Set<GridPoint2>)

class StructureTemplateProcessor(val templates: StructureTemplates) : LocationProcessor {

    override fun process(locationInfo: LocationInfo, gameEngine: Engine) {
        val tiles = locationInfo.tiles
        val entities = mutableListOf<Entity>()
        templates.templates.forEach {
            processTemplate(it, tiles, locationInfo, gameEngine, entities)
        }

        locationInfo.entities.plusAssign(entities)
    }

    private fun processTemplate(template: StructureTemplate, tiles: TileArray, locationInfo: LocationInfo, gameEngine: Engine, entities: MutableList<Entity>) {
        val random = locationInfo.random.kRandom
        val mask = template.parsedMask

        val check = random.nextDouble()
        val chance = 1.0
        val needSpawn = check <= chance

        //spawn point is the left top corner of mask
        //todo not hardcode groundTileFilter
        val spawnPoint = spawnPoint(random, tiles, mask.size, ::groundTileFilter, 10)
        if (spawnPoint == null) {
            println("spawn failed: can't get spawnPoint")
            return
        }

        val (x, y) = spawnPoint
        if (needSpawn) {
            val positions = spawnStructure(mask, tiles, x, y)
            locationInfo.structureTemplates.add(StructureTemplateInfo(template, positions))

            val entitiesInfo = template.entities
            entitiesInfo.forEach {
                spawnEntities(it, gameEngine, spawnPoint, entities)
            }

            val quests = template.quests
            quests.forEach {
                (questId, entityId) ->
                val owner = newEntity(entityId, entities)
                var qc = owner[questOwner]
                if (qc == null) {
                    qc = QuestOwnerComponent()
                    owner.add(qc)
                }
                qc.questIds.add(questId)
            }
        }
    }

    private fun spawnEntities(e: StructureEntity, gameEngine: Engine, spawnPoint: GridPoint2, entities: MutableList<Entity>) {
        val (templateName, points, ids) = e
        val entityTemplate = entityTemplate(name = templateName)
        points.forEachIndexed {
            i, point ->
            val (x, y) = point
            val id = if (i < ids.size) ids[i] else randomId()
            val entity = newTemplatedEntity(id, entityTemplate, gameEngine)
            entity[position]?.gridPosition = point(spawnPoint.x + x, spawnPoint.y - y)

            entities.add(entity)
        }
    }

    private fun spawnStructure(mask: List<List<Tile>>, tiles: TileArray, x: Int, y: Int): Set<GridPoint2> {
        val result = mutableSetOf<GridPoint2>()
        mask.forEachIndexed { i, _ ->
            mask[i].forEachIndexed { j, _ ->
                val tile = mask[i][j]
                if (tile != whateverTileId) {
                    val resultX = x + j
                    val resultY = y - i

                    tiles[resultX, resultY] = tile
                    result.add(point(resultX, resultY))
                }
            }
        }
        return result
    }
}