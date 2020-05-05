package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.component1
import com.ovle.rll3.component2
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.special.LevelDescription
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.entity.newTemplatedEntity
import com.ovle.rll3.model.template.entity.entityTemplate
import com.ovle.rll3.model.template.structure.StructureTemplate
import com.ovle.rll3.model.template.structure.StructureTemplates
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.TileType
import com.ovle.rll3.model.tile.whateverTileId
import com.ovle.rll3.point
import ktx.ashley.get

data class StructureInfo(val template: StructureTemplate, val positions: Set<GridPoint2>)

class StructureProcessor(val templates: StructureTemplates) : TilesProcessor {

    override fun process(levelInfo: LevelInfo, gameEngine: Engine, levelDescription: LevelDescription) {
        val tiles = levelInfo.tiles
        val entities = mutableListOf<Entity>()

        //todo
        val template = templates.templates.singleOrNull { it.name == "house 1" } ?: return
        val mask = template.parsedMask
        val maskWidth = mask.size
        val maskHeight = mask.maxBy { it.size }!!.size

        val check = Math.random()
        val chance = 1.0
        val needSpawn = check <= chance

        //spawn point is the left top corner of mask
        val spawnPoint = spawnPoint()
        val (x, y) = spawnPoint
        if (needSpawn) {
            val positions = spawnStructure(mask, tiles, y, x)
            levelInfo.structures.add(StructureInfo(template, positions))

            val entitiesInfo = template.entities
            entitiesInfo.forEach{
                (templateName, points) -> spawnEntities(templateName, points, gameEngine, spawnPoint, entities)
            }
        }

        levelInfo.objects.plusAssign(entities)
    }

    private fun spawnEntities(templateName: String, points: Array<Pair<Int, Int>>, gameEngine: Engine, spawnPoint: GridPoint2, entities: MutableList<Entity>) {
        val entityTemplate = entityTemplate(name = templateName)
        points.forEach {(x, y) ->
            val entity = newTemplatedEntity(entityTemplate, gameEngine)
            entity[Mappers.position]?.gridPosition = point(spawnPoint.x + x, spawnPoint.y - y)
            entities.add(entity)
        }
    }

    private fun spawnStructure(mask: List<List<TileType>>, tiles: TileArray, y: Int, x: Int): Set<GridPoint2> {
        val result = mutableSetOf<GridPoint2>()
        mask.forEachIndexed { i, _ ->
            mask[i].forEachIndexed { j, _ ->
                val tileType = mask[i][j]
                if (tileType != whateverTileId) {
                    val resultX = y + j
                    val resultY = x - i

                    tiles.setTile(resultX, resultY, Tile(tileType))
                    result.add(point(resultX, resultY))
                }
            }
        }
        return result
    }

    //todo
    private fun spawnPoint() = point(10, 10)
}