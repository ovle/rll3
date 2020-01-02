package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch


abstract class EventSystem <T: Event>: EntitySystem(), CoroutineScope by GlobalScope {

    lateinit var channel: ReceiveChannel<T>

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)

        launch {
            channel = channel()
            for (event in channel) {
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