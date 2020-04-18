package com.ovle.rll3.event

import kotlin.reflect.KClass

//todo async?
//todo coroutines?
object EventBus {

    val subscribers = mutableMapOf<KClass<Event>, MutableCollection<(Event) -> Unit>>()
    private val hooks: MutableCollection<(Event) -> Unit> = mutableListOf()

    fun addHook(hook: (Event) -> Unit) {
        hooks.add(hook)
    }

    fun clearHooks() = hooks.clear()

    fun send(event: Event) {
        hooks.forEach { it.invoke(event) }
        subscribers[event.javaClass.kotlin]?.forEach {
            it.invoke(event)
        }
    }

    inline fun <reified T : Event> subscribe(noinline callback: (T) -> Unit) {
        val clazz = T::class as KClass<Event>
        val eventSubscribers = subscribers[clazz]
            ?: mutableListOf<(Event) -> Unit>().apply { subscribers[clazz] = this }
        eventSubscribers.add(callback as (Event) -> Unit)
    }

    fun clearSubscriptions() = subscribers.clear()
}