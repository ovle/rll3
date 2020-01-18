package com.ovle.rll3

//import model.game.chunk.Chunk
//import model.game.entity.Entity
//import model.game.entity.action.Action
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.LevelInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.launch

sealed class Event {
//    open class EntityEvent(val entity: Entity) : Event()
//    class EntityStartMove(entity: Entity) : EntityEvent(entity)
//    class EntityFinishMove(entity: Entity) : EntityEvent(entity)
//    class EntityMoved(entity: Entity, val dx: Double, val dy: Double) : EntityEvent(entity)
//    class EntityDidAction(entity: Entity, val action: Action) : EntityEvent(entity)
//    class ChunksLoaded(val chunks: Collection<Chunk>) : Event()
//    class ChunksUnloaded(val chunks: Collection<Chunk>) : Event()
//    class EntitiesLoaded(val entitiesWith: Collection<Entity>) : Event()
//    class EntitiesUnloaded(val entitiesWith: Collection<Entity>) : Event()

    open class PlayerControlEvent : Event()
    class MouseMoved(val screenPoint: Vector2) : PlayerControlEvent()
    class MouseLeftClick(val screenPoint: Vector2) : PlayerControlEvent()
    class CameraScaleInc: PlayerControlEvent()
    class CameraScaleDec: PlayerControlEvent()
    class CameraScrolled(val amount: Int): PlayerControlEvent()
    class CameraMoved(val amount: Vector2): PlayerControlEvent()
    //test
    open class LevelActionEvent: PlayerControlEvent()
    class NextLevelEvent: LevelActionEvent()
    class PrevLevelEvent: LevelActionEvent()
    class SaveLevelEvent: LevelActionEvent()
    class LoadLevelEvent: LevelActionEvent() //todo

    open class GameEvent : Event()
    open class EntityEvent(val entity: Entity) : GameEvent()
    open class EntityMoved(entity: Entity) : EntityEvent(entity)
    class LevelUnloaded(val level: LevelInfo): GameEvent()
    class LevelLoaded(val level: LevelInfo): GameEvent()
}

//fun chunksLoaded(chunks: Collection<Chunk>) = Messenger.publish(Event.ChunksLoaded(chunks))
//fun chunksUnloaded(chunks: Collection<Chunk>) = Messenger.publish(Event.ChunksUnloaded(chunks))
//fun entitiesLoaded(entitiesWith: Collection<Entity>) = Messenger.publish(Event.EntitiesLoaded(entitiesWith))
//fun entitiesUnloaded(entitiesWith: Collection<Entity>) = Messenger.publish(Event.EntitiesUnloaded(entitiesWith))
//fun entityStartMove(entity: Entity) = Messenger.publish(Event.EntityStartMove(entity))
//fun entityFinishMove(entity: Entity) = Messenger.publish(Event.EntityFinishMove(entity))
//fun entityMoved(entity: Entity,  dx: Double, dy: Double) = Messenger.publish(Event.EntityMoved(entity, dx, dy))
//fun entityDidAction(entity: Entity, action: Action) = Messenger.publish(Event.EntityDidAction(entity, action))
//
//fun onChunksLoaded(callback: (Event.ChunksLoaded) -> Unit) = Messenger.subscribe<Event.ChunksLoaded>(callback)
//fun onChunksUnloaded(callback: (Event.ChunksUnloaded) -> Unit) = Messenger.subscribe<Event.ChunksUnloaded>(callback)
//fun onEntitiesLoaded(callback: (Event.EntitiesLoaded) -> Unit) = Messenger.subscribe<Event.EntitiesLoaded>(callback)
//fun onEntitiesUnloaded(callback: (Event.EntitiesUnloaded) -> Unit) = Messenger.subscribe<Event.EntitiesUnloaded>(callback)
//fun onEntityStartMove(callback: (Event.EntityStartMove) -> Unit) = Messenger.subscribe<Event.EntityStartMove>(callback)
//fun onEntityFinishMove(callback: (Event.EntityFinishMove) -> Unit) = Messenger.subscribe<Event.EntityFinishMove>(callback)
//fun onEntityMoved(callback: (Event.EntityMoved) -> Unit) = Messenger.subscribe<Event.EntityMoved>(callback)
//fun onEntityDidAction(callback: (Event.EntityDidAction) -> Unit) = Messenger.subscribe<Event.EntityDidAction>(callback)

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