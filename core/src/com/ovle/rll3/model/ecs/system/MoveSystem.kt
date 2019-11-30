package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.Event
import com.ovle.rll3.model.ecs.component.MoveComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import ktx.ashley.get
import kotlin.math.abs


class MoveSystem : IteratingSystem(all(MoveComponent::class.java, PositionComponent::class.java).get()), CoroutineScope by GlobalScope {
    private val move: ComponentMapper<MoveComponent> = get()
    private val position: ComponentMapper<PositionComponent> = get()
    private val stopDelta = 0.5f

    lateinit var channel: ReceiveChannel<Event.PlayerControlEvent>

    //todo !!!!!!!!!!!
    private var moveTarget: Vector2? = null

    //    todo move these to separate class
//    override fun addedToEngine(engine: Engine?) {
//        super.addedToEngine(engine)
//
//        launch {
//            channel = EventBus.receive()
//            for (event in channel) {
//                println(event)
//                dispatch(event)
//            }
//        }
//    }
//    //    todo move these to separate class
//    override fun removedFromEngine(engine: Engine?) {
//        super.removedFromEngine(engine)
//        channel.cancel()
//    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val moveComponent = entity[move]!!
        if (moveTarget != null) {
            moveComponent.add(moveTarget!!)
            moveTarget = null
            if (!moveComponent.started()) moveComponent.start()
        }

        if (!moveComponent.started()) return

        move(entity, deltaTime)
    }

    //todo subsribe on mose click, set target
    private fun move(entity: Entity, deltaTime: Float) {
        val moveComponent = entity[move]!!
        val positionComponent = entity[position]!!
        val timePercent = moveComponent.tilesPerSecond * deltaTime
//        val currentFrom = moveComponent.currentFrom() ?: return
        val currentTo = moveComponent.currentTo() ?: return
        val currentPosition = positionComponent.position

        val dx = if (currentTo.x > currentPosition.x) timePercent else -timePercent
        val dy = if (currentTo.y > currentPosition.y) timePercent else -timePercent
        currentPosition.add(dx, dy)

        val distanceToTarget = abs(currentPosition.dst(currentTo))
        val moveFinished = distanceToTarget <= stopDelta
        if (moveFinished) {
            moveComponent.next()
        }
        val pathFinished = moveComponent.finished()
        if (pathFinished) {
            moveComponent.reset()
        }
    }

    private fun dispatch(event: Event.PlayerControlEvent) {
        when (event) {
            is Event.MouseLeftClick -> onMoveTargetSet(event.screenPoint)
        }
    }

    private fun onMoveTargetSet(screenPoint: Vector2) {
        //todo
//        moveTarget = toGamePoint(screenPoint, renderConfig = )
    }
}