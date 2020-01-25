package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.ovle.rll3.Event.*
import com.ovle.rll3.EventBus.receive
import com.ovle.rll3.EventBus.send
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.*
import com.ovle.rll3.model.ecs.component.*
import com.ovle.rll3.model.procedural.dungeonGenerationSettings
import com.ovle.rll3.model.procedural.grid.DungeonGridFactory
import com.ovle.rll3.model.procedural.grid.GridFactory
import com.ovle.rll3.model.procedural.grid.processor.DoorProcessor
import com.ovle.rll3.model.procedural.grid.processor.LightSourceProcessor
import com.ovle.rll3.model.procedural.grid.processor.RoomStructureProcessor
import com.ovle.rll3.model.procedural.grid.processor.RoomsInfoProcessor
import com.ovle.rll3.model.procedural.mapSizeInTiles
import com.ovle.rll3.model.tile.TilePassType
import com.ovle.rll3.model.util.entityTilePassMapper
import com.ovle.rll3.model.util.gridToTileArray
import ktx.ashley.get


class LevelSystem: EventSystem<LevelActionEvent>() {

    private val levelMapper: ComponentMapper<LevelComponent> = componentMapper()

    override fun channel() = receive<LevelActionEvent>()

    override fun dispatch(event: LevelActionEvent) {
        val levelInfo = levelInfoNullable()
        engine.removeAllEntities()

        val newLevelInfo = when (event) {
            is PrevLevelEvent -> prevLevelInfo(levelInfo)
            is NextLevelEvent -> nextLevelInfo(levelInfo) ?: newLevelInfo(mapSizeInTiles, DungeonGridFactory())
            else -> null
        }

        newLevelInfo?.let {
            val level = entityWithNullable(allEntities().toList(), LevelComponent::class)
                ?: engine.entity(LevelComponent(newLevelInfo))    //todo rewrite
            level[levelMapper]?.level = it

//            send(LevelUnloaded(it))
            send(LevelLoaded(it))
        }
    }

    //todo
    private fun prevLevelInfo(level: LevelInfo?): LevelInfo? {
        return null
    }

    //todo
    private fun nextLevelInfo(level: LevelInfo?): LevelInfo? {
        return null
    }

    //todo move out
    private fun newLevelInfo(size: Int, gridFactory: GridFactory): LevelInfo {
        val grid = gridFactory.get(size, dungeonGenerationSettings)
        return gridToTileArray(grid)
            .run {
                val result = LevelInfo(tiles = this)

                //todo inject list by interface
                RoomsInfoProcessor().process(result, engine)
                RoomStructureProcessor().process(result, engine)
                DoorProcessor().process(result, engine)
                LightSourceProcessor().process(result, engine)
//                TrapProcessor().process(result, engine)

                //todo move out
                val startTile = this.indexedTiles().find {
                    entityTilePassMapper(it.second) == TilePassType.Passable
                }!!
                val startPosition = this.point(startTile.first)
                engine.entity(
                    PlayerControlledComponent(),
                    PositionComponent(floatPoint(startPosition)),
                    MoveComponent(),
                    SightComponent(5),
                    RenderComponent(),
                    AnimationComponent()
                )

                result
            }
    }
}