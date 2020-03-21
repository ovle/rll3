package com.ovle.rll3.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.AnimationType
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.component.WorldInfo
import com.ovle.rll3.model.ecs.system.event.level.ConnectionId
import com.ovle.rll3.model.procedural.config.LevelParams

sealed class Event {

    open class PlayerControlEvent : Event()
    class MouseMoved(val screenPoint: Vector2) : PlayerControlEvent()
    class MouseLeftClick(val screenPoint: Vector2) : PlayerControlEvent()
    class CameraScaleInc: PlayerControlEvent()
    class CameraScaleDec: PlayerControlEvent()
    class CameraScrolled(val amount: Int): PlayerControlEvent()
    class CameraMoved(val amount: Vector2): PlayerControlEvent()

    open class LevelEditEvent: Event()
    class LightSwitchEvent: LevelEditEvent() //todo

    open class GameEvent: Event()
    open class GlobalGameEvent: Event()
    open class GameStartedEvent: GlobalGameEvent()
    open class GameFinishedEvent: GlobalGameEvent()
    open class WorldInitEvent(val world: WorldInfo) : GlobalGameEvent()

    open class EntityEvent(val entity: Entity) : GameEvent()
    open class EntityInteractionEvent(entity: Entity) : EntityEvent(entity)

    open class EntityInitialized(entity: Entity) : EntityEvent(entity)
    open class EntityStartMove(entity: Entity) : EntityEvent(entity)
    open class EntityMoved(entity: Entity) : EntityEvent(entity)
    open class EntityFinishMove(entity: Entity) : EntityEvent(entity)

    open class EntityLevelTransition(entity: Entity, val connectionId: ConnectionId) : EntityEvent(entity)
    class LevelUnloaded(val level: LevelInfo): GameEvent()
    class LevelLoaded(val level: LevelInfo, val levelParams: LevelParams, val playerEntity: Entity): GameEvent()

    open class EntityAnimationEvent(entity: Entity, val type: AnimationType) : EntityEvent(entity)
    class EntityAnimationStartEvent(entity: Entity, type: AnimationType): EntityAnimationEvent(entity, type)
    class EntityAnimationStopEvent(entity: Entity, type: AnimationType): EntityAnimationEvent(entity, type) //todo remove
}
