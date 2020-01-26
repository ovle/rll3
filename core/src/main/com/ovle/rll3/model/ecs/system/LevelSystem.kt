package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.Event.*
import com.ovle.rll3.EventBus.receive
import com.ovle.rll3.EventBus.send
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.*
import com.ovle.rll3.model.ecs.component.*
import com.ovle.rll3.model.ecs.system.level.levelInfo
import com.ovle.rll3.model.procedural.dungeonGenerationSettings
import com.ovle.rll3.model.procedural.grid.DungeonGridFactory
import com.ovle.rll3.model.procedural.mapSizeInTiles
import com.ovle.rll3.model.tile.TilePassType
import com.ovle.rll3.model.util.entityTilePassMapper
import ktx.ashley.get


class LevelSystem: EventSystem<LevelActionEvent>() {

    private val levelMapper: ComponentMapper<LevelComponent> = componentMapper()

    override fun channel() = receive<LevelActionEvent>()

    override fun dispatch(event: LevelActionEvent) {
        val levelInfo = levelInfoNullable()
        engine.removeAllEntities()

        val newLevelInfo = when (event) {
            is PrevLevelEvent -> prevLevelInfo(levelInfo)
            is NextLevelEvent -> nextLevelInfo(levelInfo)
                ?: levelInfo(mapSizeInTiles, DungeonGridFactory(), dungeonGenerationSettings, engine)
            else -> null
        }

        newLevelInfo?.let {
            val level = entityWithNullable(allEntities().toList(), LevelComponent::class)
                ?: engine.entity(LevelComponent(newLevelInfo))    //todo rewrite

            initPlayerForLevel(level[levelMapper]!!.level, engine)

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

    private fun initPlayerForLevel(level: LevelInfo, engine: Engine) {
        val player = engine.entity(
            PositionComponent(playerStartPosition(level)),
            MoveComponent(),
            SightComponent(5),
            RenderComponent(),
            AnimationComponent()
        )

        engine.entity(
            PlayerInteractionComponent(
                controlledEntity = player
//                focusedEntity = player
            ),
            RenderComponent(),
            PositionComponent()
        )
    }

    private fun playerStartPosition(level: LevelInfo): Vector2 {
        val tiles = level.tiles
        val startTile = tiles.indexedTiles().find {
            entityTilePassMapper(it.second) == TilePassType.Passable
        }!!
        return floatPoint(tiles.point(startTile.first))
    }
}