package com.ovle.rll3

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.LevelInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.launch

sealed class Event {

    open class PlayerControlEvent : Event()
    class MouseMoved(val screenPoint: Vector2) : PlayerControlEvent()
    class MouseLeftClick(val screenPoint: Vector2) : PlayerControlEvent()
    class CameraScaleInc: PlayerControlEvent()
    class CameraScaleDec: PlayerControlEvent()
    class CameraScrolled(val amount: Int): PlayerControlEvent()
    class CameraMoved(val amount: Vector2): PlayerControlEvent()
    //test
    open class LevelActionEvent: Event()
    open class LevelEditEvent: Event()

    class NextLevelEvent: LevelActionEvent()
    class PrevLevelEvent: LevelActionEvent()
    class SaveLevelEvent: LevelActionEvent()
    class LoadLevelEvent: LevelActionEvent()

    class LightSwitchEvent: LevelEditEvent() //todo

    open class GameEvent : Event()
    open class EntityEvent(val entity: Entity) : GameEvent()
    open class EntityMoved(entity: Entity) : EntityEvent(entity)
    class LevelUnloaded(val level: LevelInfo): GameEvent()
    class LevelLoaded(val level: LevelInfo): GameEvent()

    open class EntityAnimationEvent(entity: Entity, val animationId: String) : EntityEvent(entity)
    class EntityAnimationStartEvent(entity: Entity, animationId: String): EntityAnimationEvent(entity, animationId)
    class EntityAnimationStopEvent(entity: Entity, animationId: String): EntityAnimationEvent(entity, animationId)
}

@ExperimentalCoroutinesApi
object EventBus : CoroutineScope by GlobalScope {
    val bus: BroadcastChannel<Any> = ConflatedBroadcastChannel()

    fun send(o: Any) {
        launch {
            bus.send(o)
//            println("send: $o")
        }
    }

    inline fun <reified T> receive(): ReceiveChannel<T> {
        return bus.openSubscription().filter { it is T }.map { it as T }
    }
}