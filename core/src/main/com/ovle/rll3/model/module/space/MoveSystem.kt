package com.ovle.rll3.model.module.space

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.EventBus.subscribe
import com.ovle.rll3.model.module.core.component.ComponentMappers.carrier
import com.ovle.rll3.model.module.core.component.ComponentMappers.entityAction
import com.ovle.rll3.model.module.core.component.ComponentMappers.move
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.entity.bodyObstacles
import com.ovle.rll3.model.module.core.entity.locationInfo
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rlUtil.gdx.math.point
import com.ovle.rll3.model.util.path
import ktx.ashley.get
import ktx.ashley.has
import kotlin.math.abs

//todo to manage speed
//todo moving as the part of skills
class MoveSystem : EventSystem() {

    override fun subscribe() {
        subscribe<EntityStartMoveCommand> { onEntitySetMoveTargetEvent(it.entity, it.point) }
        subscribe<EntityMoveCommand> { onEntityMoveCommand(it.entity) }
        subscribe<EntityDiedEvent> { onEntityDiedEvent(it.entity) }
    }

    private fun onEntityMoveCommand(entity: Entity) {
        val moveComponent = entity[move]!!
        if (!moveComponent.path.started) return

        checkMove(entity)
    }

    private fun onEntitySetMoveTargetEvent(entity: Entity, point: GridPoint2) {
        setMoveTarget(locationInfo(), point, entity)
    }

    private fun onEntityDiedEvent(entity: Entity) {
        val moveComponent = entity[move] ?: return
        val movePath = moveComponent.path
        movePath.reset()
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
                actionComponent.turnsLeft = moveComponent.turnsPerTile
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
        val obstacles = locationInfo().entities.bodyObstacles()
        //should we finish move at that point, or consider obstacle to be temporary? or try to make another path?
        if (newPosition in obstacles) return

        positionComponent.gridPosition = newPosition

        send(EntityMovedEvent(entity))

        if (entity.has(carrier)) {
            processCarrier(entity, currentPosition)
        }
    }

    private fun processCarrier(entity: Entity, currentPosition: GridPoint2) {
        val carriedEntity = entity[carrier]!!.item ?: return
        carriedEntity[position]!!.gridPosition = currentPosition.cpy()

        send(EntityMovedEvent(carriedEntity))
    }

    private fun setMoveTarget(location: LocationInfo, to: GridPoint2, entity: Entity) {
        val tiles = location.tiles
        if (!tiles.isPointValid(to.x, to.y)) return

        val moveComponent = entity[move] ?: return
        val positionComponent = entity[position]!!
        val from = positionComponent.gridPosition

        val newPath = path(from, to, location)

        val movePath = moveComponent.path
        if (newPath.isEmpty()) {
//            println("no path found from $from to $to!")
            movePath.reset()
        } else {
            movePath.set(newPath)

            if (!movePath.started) {
                movePath.start()
                send(EntityStartedMoveEvent(entity))
            }
        }
    }
}