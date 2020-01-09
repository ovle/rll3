package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.Event.*
import com.ovle.rll3.EventBus.receive
import com.ovle.rll3.model.ecs.allEntities
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.component.MoveComponent
import com.ovle.rll3.model.ecs.component.PlayerControlledComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.entityWithNullable
import com.ovle.rll3.model.ecs.componentMapper
import com.ovle.rll3.model.ecs.levelInfoNullable
import com.ovle.rll3.model.tile.entityTilePassMapper
import com.ovle.rll3.model.tile.vectorCoords
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.model.util.pathfinding.aStar.path
import com.ovle.rll3.model.util.pathfinding.cost
import com.ovle.rll3.model.util.pathfinding.heuristics
import com.ovle.rll3.toGamePoint
import ktx.ashley.get


class PlayerControlsSystem : EventSystem<PlayerControlEvent>() {
    private val move: ComponentMapper<MoveComponent> = componentMapper()

    private val position: ComponentMapper<PositionComponent> = componentMapper()
    private val selectedGamePoint = Vector2()

    override fun channel() = receive<PlayerControlEvent>()

    override fun dispatch(event: PlayerControlEvent) {
        val level = levelInfoNullable() ?: return
        when (event) {
            is MouseLeftClick -> onMoveTargetSet(toGamePoint(event.screenPoint, RenderConfig), level)
            is MouseMoved -> onMousePositionChange(toGamePoint(event.screenPoint, RenderConfig), level)
        }
    }

    private fun onMoveTargetSet(gamePoint: Vector2, level: LevelInfo) {
        if (!isValid(gamePoint, level)) return

        val playerEntity = entityWithNullable(allEntities().toList(), PlayerControlledComponent::class)
            ?: return
        val moveComponent = playerEntity[move] ?: return
        val positionComponent = playerEntity[position]!!

        val tiles = level.tiles
        val playerPosition = positionComponent.position
        val fromTile = tiles[playerPosition.x.toInt(), playerPosition.y.toInt()]!!
        val toTile = tiles[gamePoint.x.toInt(), gamePoint.y.toInt()] ?: return

        val path = path(
            fromTile,
            toTile,
            tiles,
            heuristicsFn = ::heuristics,
            costFn = ::cost,
            tilePassTypeFn = ::entityTilePassMapper
        )

        val movePath = moveComponent.path
        movePath.set(path.map(::vectorCoords))
        if (!movePath.started()) movePath.start()
    }

    private fun onMousePositionChange(gamePoint: Vector2, level: LevelInfo) {
        if (!isValid(gamePoint, level)) return
        if (gamePoint.epsilonEquals(selectedGamePoint)) return

        selectedGamePoint.set(gamePoint.x, gamePoint.y)
    }

    private fun isValid(gamePoint: Vector2, level: LevelInfo): Boolean {
        return level.tiles.isIndexValid(gamePoint.x.toInt(), gamePoint.y.toInt())
    }
}