package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.Event
import com.ovle.rll3.EventBus
import com.ovle.rll3.model.ecs.component.MoveComponent
import com.ovle.rll3.model.ecs.component.PlayerControlledComponent
import com.ovle.rll3.model.ecs.get
import com.ovle.rll3.toGamePoint
import com.ovle.rll3.view.renderConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import ktx.ashley.get


class PlayerControlsSystem : IteratingSystem(all(PlayerControlledComponent::class.java).get()), CoroutineScope by GlobalScope {

    private val move: ComponentMapper<MoveComponent> = get()

    lateinit var channel: ReceiveChannel<Event.PlayerControlEvent>


    //    todo move these to separate class
    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)

        launch {
            channel = EventBus.receive()
            for (event in channel) {
                println(event)
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

    private fun dispatch(event: Event.PlayerControlEvent, engine: Engine) {
        when (event) {
            is Event.MouseLeftClick -> onMoveTargetSet(toGamePoint(event.screenPoint, renderConfig), engine)
        }
    }

    private fun onMoveTargetSet(gamePoint: Vector2, engine: Engine) {
        println("gamePoint $gamePoint")

        val family = all(PlayerControlledComponent::class.java)
        val playerEntity = engine.getEntitiesFor(family.get()).singleOrNull() ?: return
        val moveComponent = playerEntity[move] ?: return

        moveComponent.add(gamePoint)
        if (!moveComponent.started()) moveComponent.start()
    }
}