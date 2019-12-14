package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.Event.*
import com.ovle.rll3.EventBus
import com.ovle.rll3.model.ecs.component.*
import com.ovle.rll3.model.ecs.get
import com.ovle.rll3.model.tile.entityTilePassMapper
import com.ovle.rll3.model.tile.vectorCoords
import com.ovle.rll3.model.util.pathfinding.cost
import com.ovle.rll3.model.util.pathfinding.heuristics
import com.ovle.rll3.model.util.pathfinding.aStar.path
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
                dispatch(event, engine)
            }
        }
    }

    //    todo move these to separate class
    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        channel.cancel()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {}

    private fun dispatch(event: PlayerControlEvent, engine: Engine) {
        when (event) {
            is MouseLeftClick -> onMoveTargetSet(toGamePoint(event.screenPoint, renderConfig), engine)
            is MouseMoved -> onMousePositionChange(toGamePoint(event.screenPoint, renderConfig))
        }
    }

    private fun onMoveTargetSet(gamePoint: Vector2, engine: Engine) {
        if (!isValid(gamePoint)) return
        println("gamePoint $gamePoint")

        val family = all(PlayerControlledComponent::class.java)
        val playerEntity = engine.getEntitiesFor(family.get()).singleOrNull() ?: return
        val moveComponent = playerEntity[move] ?: return
        val positionComponent = playerEntity[position]!!

        val tiles = tileMap
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

        moveComponent.set(path.map(::vectorCoords))
        if (!moveComponent.started()) moveComponent.start()
    }

    private fun onMousePositionChange(gamePoint: Vector2) {
        if (!isValid(gamePoint)) return
        if (gamePoint.epsilonEquals(selectedGamePoint)) return

        selectedGamePoint.set(gamePoint.x, gamePoint.y)

        val tiles = tileMap
//        println("tile ${tiles[gamePoint.x.toInt(), gamePoint.y.toInt()]}")
    }

    private fun isValid(gamePoint: Vector2): Boolean {
        val tiles = tileMap
        return tiles.isIndexValid(gamePoint.x.toInt(), gamePoint.y.toInt())
    }
}