package com.ovle.rll3.model.module.space

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.game.LevelInfo
import com.ovle.rll3.model.module.core.component.ComponentMappers.entityAction
import com.ovle.rll3.model.module.core.component.ComponentMappers.move
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.entity.*
import com.ovle.rll3.model.tile.tilePassType
import com.ovle.rll3.model.util.pathfinding.aStar.path
import com.ovle.rll3.model.util.pathfinding.cost
import com.ovle.rll3.model.util.pathfinding.heuristics
import com.ovle.rll3.point
import ktx.ashley.get
import kotlin.math.abs


class MoveSystem : IteratingSystem(all(MoveComponent::class.java, PositionComponent::class.java).get()) {

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        subscribe()
    }

    fun subscribe() {
        EventBus.subscribe<EntityStartMoveCommand> { onEntitySetMoveTargetEvent(it.entity, it.point) }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val moveComponent = entity[move]!!
        if (!moveComponent.path.started) return

        checkMove(entity)
    }


    private fun onEntitySetMoveTargetEvent(entity: Entity, point: GridPoint2) {
        setMoveTarget(levelInfo(), point, entity)
    }

    private fun checkMove(entity: Entity) {
        val positionComponent = entity[position]!!
        val moveComponent = entity[move]!!
        val movePath = moveComponent.path
        val currentTarget = movePath.currentTarget ?: return

        val gridPosition = positionComponent.gridPosition
        val moveFinished = currentTarget == gridPosition
        if (moveFinished) {
            movePath.next()
        }
        val pathFinished = movePath.finished
        if (pathFinished) {
            movePath.reset()
            send(EntityFinishedMoveEvent(entity))
        } else {
            val actionComponent = entity[entityAction]!!
            if (actionComponent.current == null) {
                actionComponent.current = { move(entity) }
                actionComponent.timeLeft = moveComponent.ticksPerTile
            }
        }
    }

    private fun move(entity: Entity) {
        val positionComponent = entity[position]!!
        val moveComponent = entity[move]!!

        val currentPosition = positionComponent.gridPosition
        val currentTarget = moveComponent.path.currentTarget ?: return

        val dx = currentTarget.x - currentPosition.x
        val dy = currentTarget.y - currentPosition.y
        val useX = abs(dx) > abs(dy)
        val dxStep = when {
            !useX -> 0
            dx > 0 -> 1
            dx == 0 -> 0
            else -> -1
        }
        val dyStep = when {
            useX -> 0
            dy > 0 -> 1
            dy == 0 -> 0
            else -> -1
        }

        val newPosition = point(currentPosition.x + dxStep, currentPosition.y + dyStep)
        //it can be expensive to do that on every move processing. cache?
        val obstacles = levelInfo().entities.bodyObstacles()
        //should we finish move at that point, or consider obstacle to be temporary? or try to make another path?
        if (newPosition in obstacles) return

        positionComponent.gridPosition = newPosition

        send(EntityMovedEvent(entity))
    }

    private fun setMoveTarget(level: LevelInfo, to: GridPoint2, entity: Entity) {
        val tiles = level.tiles
        if (!tiles.isPointValid(to.x, to.y)) return

        val moveComponent = entity[move] ?: return
        val positionComponent = entity[position]!!

        val from = positionComponent.gridPosition
        val path = path(
            from,
            to,
            tiles,
            level.entities.bodyObstacles(),
            heuristicsFn = ::heuristics,
            costFn = ::cost,
            tilePassTypeFn = ::tilePassType
        )

        if (path.isEmpty()) {
            println("no path found from $from to $to!")
            return
        }

        val movePath = moveComponent.path
        movePath.set(path)

        if (!movePath.started) {
            movePath.start()
            send(EntityStartedMoveEvent(entity))
        }
    }
}