package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.TileType
import com.ovle.rll3.component1
import com.ovle.rll3.component2
import com.ovle.rll3.model.ecs.component.advanced.QuestOwnerComponent
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.ecs.component.dto.WorldInfo
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.Mappers.questOwner
import com.ovle.rll3.model.ecs.entity.entity
import com.ovle.rll3.model.ecs.entity.newTemplatedEntity
import com.ovle.rll3.model.ecs.entity.randomId
import com.ovle.rll3.model.template.entity.entityTemplate
import com.ovle.rll3.model.template.structure.StructureEntity
import com.ovle.rll3.model.template.structure.StructureTemplate
import com.ovle.rll3.assets.loader.StructureTemplates
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.whateverTileId
import com.ovle.rll3.model.util.random
import com.ovle.rll3.point
import ktx.ashley.get
import kotlin.random.Random

data class StructureInfo(val template: StructureTemplate, val positions: Set<GridPoint2>)

class StructureProcessor(val templates: StructureTemplates) : TilesProcessor {

    override fun process(levelInfo: LevelInfo, worldInfo: WorldInfo, gameEngine: Engine) {
        val tiles = levelInfo.tiles
        val entities = mutableListOf<Entity>()

        templates.templates.forEach {
            processTemplate(it, tiles, levelInfo, gameEngine, entities, random)
        }

        levelInfo.entities.plusAssign(entities)
    }

    private fun processTemplate(template: StructureTemplate, tiles: TileArray, levelInfo: LevelInfo, gameEngine: Engine, entities: MutableList<Entity>, r: Random) {
        val mask = template.parsedMask
        val maskWidth = mask.size
        val maskHeight = mask.maxBy { it.size }!!.size
        val size = tiles.size
        val widthDiff = size - maskWidth
        val heightDiff = size - maskHeight

        val check = r.nextDouble()
        val chance = 1.0
        val needSpawn = check <= chance

        //spawn point is the left top corner of mask
        val spawnPoint = spawnPoint(widthDiff, heightDiff, size, r)
        val (x, y) = spawnPoint
        if (needSpawn) {
            val positions = spawnStructure(mask, tiles, x, y)
            levelInfo.structures.add(StructureInfo(template, positions))

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

    private fun spawnStructure(mask: List<List<TileType>>, tiles: TileArray, x: Int, y: Int): Set<GridPoint2> {
        val result = mutableSetOf<GridPoint2>()
        mask.forEachIndexed { i, _ ->
            mask[i].forEachIndexed { j, _ ->
                val tileType = mask[i][j]
                if (tileType != whateverTileId) {
                    val resultX = x + j
                    val resultY = y - i

                    tiles.setTile(resultX, resultY, Tile(tileType))
                    result.add(point(resultX, resultY))
                }
            }
        }
        return result
    }

    private fun spawnPoint(widthDiff: Int, heightDiff: Int, size: Int, r: Random) = point(
        (0..widthDiff).random(r), (size - 1) - (0..heightDiff).random(r)
    )
}