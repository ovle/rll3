package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.Event.*
import com.ovle.rll3.EventBus
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.component.MoveComponent
import com.ovle.rll3.model.ecs.component.PlayerControlledComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.entityWith
import com.ovle.rll3.model.ecs.get
import com.ovle.rll3.model.ecs.levelInfo
import com.ovle.rll3.model.tile.entityTilePassMapper
import com.ovle.rll3.model.tile.vectorCoords
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.model.util.pathfinding.aStar.path
import com.ovle.rll3.model.util.pathfinding.cost
import com.ovle.rll3.model.util.pathfinding.heuristics
import com.ovle.rll3.toGamePoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import ktx.ashley.get


class PlayerControlsSystem : IteratingSystem(all(PlayerControlledComponent::class.java).get()), CoroutineScope by GlobalScope {

    private val move: ComponentMapper<MoveComponent> = get()
    private val position: ComponentMapper<PositionComponent> = get()

    lateinit var channel: ReceiveChannel<PlayerControlEvent>

    private val selectedGamePoint = Vector2()

    //    todo move these to separate class
    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)

        launch {
            channel = EventBus.receive()
            for (event in channel) {
                dispatch(event)
            }
        }
    }

    //    todo move these to separate class
    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        channel.cancel()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
//        todo no iterating processing, only events. not IteratingSystem ?
    }

    private fun dispatch(event: PlayerControlEvent) {
        val level = levelInfo()
        when (event) {
            is MouseLeftClick -> onMoveTargetSet(toGamePoint(event.screenPoint, RenderConfig), level)
            is MouseMoved -> onMousePositionChange(toGamePoint(event.screenPoint, RenderConfig), level)
        }
    }

    private fun onMoveTargetSet(gamePoint: Vector2, level: LevelInfo) {
        if (!isValid(gamePoint, level)) return

        val playerEntity = entityWith(entities.toList(), PlayerControlledComponent::class)
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