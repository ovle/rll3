package com.ovle.rll3.model.procedural.grid.processor.structure

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.*
import com.ovle.rll3.model.module.quest.QuestOwnerComponent
import com.ovle.rll3.model.module.game.LevelInfo
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.component.ComponentMappers.questOwner
import com.ovle.rll3.model.module.core.entity.entity
import com.ovle.rll3.model.module.core.entity.newTemplatedEntity
import com.ovle.rll3.model.module.core.entity.randomId
import com.ovle.rll3.model.template.entity.entityTemplate
import com.ovle.rll3.model.template.structure.StructureEntity
import com.ovle.rll3.model.template.structure.StructureTemplate
import com.ovle.rll3.assets.loader.StructureTemplates
import com.ovle.rll3.model.procedural.config.groundTileFilter
import com.ovle.rll3.model.procedural.grid.processor.TilesProcessor
import com.ovle.rll3.model.tile.whateverTileId
import ktx.ashley.get

data class StructureTemplateInfo(val template: StructureTemplate, val positions: Set<GridPoint2>)

class StructureTemplateProcessor(val templates: StructureTemplates) : TilesProcessor {

    override fun process(levelInfo: LevelInfo, gameEngine: Engine) {
        val tiles = levelInfo.tiles
        val entities = mutableListOf<Entity>()
        templates.templates.forEach {
            processTemplate(it, tiles, levelInfo, gameEngine, entities)
        }

        levelInfo.entities.plusAssign(entities)
    }

    private fun processTemplate(template: StructureTemplate, tiles: TileArray, levelInfo: LevelInfo, gameEngine: Engine, entities: MutableList<Entity>) {
        val random = levelInfo.random.kRandom
        val mask = template.parsedMask

        val check = random.nextDouble()
        val chance = 1.0
        val needSpawn = check <= chance

        //spawn point is the left top corner of mask
        //todo
        val spawnPoint = spawnPoint(random, tiles, mask.size, null /*::groundTileFilter*/, 15)
        if (spawnPoint == null) {
            println("spawn failed: can't get spawnPoint")
            return
        }

        val (x, y) = spawnPoint
        if (needSpawn) {
            val positions = spawnStructure(mask, tiles, x, y)
            levelInfo.structureTemplates.add(StructureTemplateInfo(template, positions))

            val entitiesInfo = template.entities
            entitiesInfo.forEach {
                spawnEntities(it, gameEngine, spawnPoint, entities)
            }

            val quests = template.quests
            quests.forEach {
                (questId, entityId) ->
                val owner = entity(entityId, entities)
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