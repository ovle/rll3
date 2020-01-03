package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.Event
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import ktx.async.KtxAsync


abstract class EventSystem <T: Event>: EntitySystem() {

    lateinit var channel: ReceiveChannel<T>

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)

        KtxAsync.launch {
            channel = channel()
            //todo
            for (event in channel) {
                println("dispatch: $event")
                dispatch(event)
            }
        }
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        channel.cancel()
    }

    abstract fun channel(): ReceiveChannel<T>

    abstract fun dispatch(event: T)
}