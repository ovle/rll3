package com.ovle.rll3.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.ecs.component.special.WorldInfo
import com.ovle.rll3.model.ecs.system.interaction.CombatAction
import com.ovle.rll3.model.ecs.system.level.ConnectionId
import com.ovle.rll3.model.procedural.config.LevelParams
import com.ovle.rll3.model.template.AnimationType

sealed class Event {

    open class PlayerControlEvent : Event()
    class MouseMoved(val screenPoint: Vector2) : PlayerControlEvent()
    class MouseClick(val screenPoint: Vector2, val button: Int) : PlayerControlEvent()
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
    open class TimeChanged(val turn: Long) : GameEvent()

    //debug
    open class DebugCombatEvent: Event()
    open class DebugToggleFocusEvent: Event()

    //level
    class LevelLoaded(val level: LevelInfo, val levelParams: LevelParams): GameEvent()

    //technical
    open class VoidClick(val button: Int, val point: GridPoint2) : GameEvent()
    open class LogEvent(val message: String) : GameEvent()

    //entity
    open class EntityEvent(val entity: Entity) : GameEvent()
    //entity - technical
    open class EntityClick(val button: Int, entity: Entity) : EntityEvent(entity)
    open class EntityHoverEvent(entity: Entity) : EntityEvent(entity)
    open class EntityUnhoverEvent : GameEvent()

    open class EntityInitialized(entity: Entity) : EntityEvent(entity)
    open class EntitySelectEvent(entity: Entity) : EntityEvent(entity)
    open class EntityDeselectEvent(entity: Entity) : EntityEvent(entity)
    open class ShowEntityInfoEvent(entity: Entity) : EntityEvent(entity)
    open class HideEntityInfoEvent() : GameEvent()
    open class ShowEntityActionsEvent(entity: Entity, val actions: Collection<String>) : EntityEvent(entity)
    open class HideEntityActionsEvent() : GameEvent()
    open class EntityFovUpdated(entity: Entity) : EntityEvent(entity)
    //entity - view
    open class EntityAnimationStart(entity: Entity, val animation: AnimationType, val duration: Int) : EntityEvent(entity)
    open class EntityAnimationFinish(entity: Entity, val animation: AnimationType) : EntityEvent(entity)
    //entity - model
    open class EntityActionEvent(val source: Entity, target: Entity, val action: String) : EntityEvent(target)
    open class EntityChanged(entity: Entity) : EntityEvent(entity)
    open class EntityCombatAction(entity: Entity, val action: CombatAction) : EntityEvent(entity)
    open class EntityTakeDamage(entity: Entity, val source: Entity?, val amount: Int, val blockedAmount: Int) : EntityEvent(entity)
    open class EntityDied(entity: Entity) : EntityEvent(entity)
    open class EntityLevelTransition(entity: Entity, val connectionId: ConnectionId) : EntityEvent(entity)
    open class EntityStartMove(entity: Entity) : EntityEvent(entity)
    open class EntityMoved(entity: Entity) : EntityEvent(entity)
    open class EntityFinishMove(entity: Entity) : EntityEvent(entity)

}
