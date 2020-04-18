package com.ovle.rll3.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.EntityInteraction
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.ecs.component.special.WorldInfo
import com.ovle.rll3.model.ecs.system.level.ConnectionId
import com.ovle.rll3.model.procedural.config.LevelParams

sealed class Event {

    open class PlayerControlEvent : Event()
    class MouseMoved(val screenPoint: Vector2) : PlayerControlEvent()
    class MouseLeftClick(val screenPoint: Vector2) : PlayerControlEvent()
    class CameraScaleInc: PlayerControlEvent()
    class CameraScaleDec: PlayerControlEvent()
    class CameraScrolled(val amount: Int): PlayerControlEvent()
    class CameraMoved(val amount: Vector2): PlayerControlEvent()

    //global
    open class GameEvent: Event()
    open class GlobalGameEvent: Event()
    open class GameStartedEvent: GlobalGameEvent()
    open class GameFinishedEvent: GlobalGameEvent()
    open class WorldInitEvent(val world: WorldInfo) : GlobalGameEvent()

    //level
    class LevelLoaded(val level: LevelInfo, val levelParams: LevelParams): GameEvent()

    //entity
    open class EntityEvent(val entity: Entity) : GameEvent()
    //entity - technical
    open class EntityInitialized(entity: Entity) : EntityEvent(entity)
    open class EntityUnselectEvent : GameEvent()
    open class EntitySelectEvent(entity: Entity) : EntityEvent(entity)
    open class EntityStartMove(entity: Entity) : EntityEvent(entity)
    open class EntityMoved(entity: Entity) : EntityEvent(entity)
    open class EntityFinishMove(entity: Entity) : EntityEvent(entity)
    //entity - model
    open class EntityInteractionEvent(entity: Entity, val interaction: EntityInteraction) : EntityEvent(entity)
    open class EntityTakeDamage(entity: Entity, val source: Entity?, val amount: Int) : EntityEvent(entity)
    open class EntityDied(entity: Entity) : EntityEvent(entity)
    open class EntityLevelTransition(entity: Entity, val connectionId: ConnectionId) : EntityEvent(entity)
}
