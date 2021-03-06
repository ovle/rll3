package com.ovle.rll3.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.ecs.component.special.PlayerInfo
import com.ovle.rll3.model.ecs.component.special.WorldInfo
import com.ovle.rll3.model.ecs.system.interaction.EntityInteraction
import com.ovle.rll3.model.ecs.system.interaction.EntityInteractionType
import com.ovle.rll3.model.ecs.system.interaction.skill.SkillTemplate
import com.ovle.rll3.model.ecs.system.level.ConnectionId
import com.ovle.rll3.model.ecs.system.quest.QuestInfo
import com.ovle.rll3.model.procedural.config.LevelParams
import com.ovle.rll3.model.template.entity.view.AnimationType

sealed class Event {

    open class PlayerControlEvent : Event()
    class MouseMoved(val screenPoint: Vector2) : PlayerControlEvent()
    class MouseClick(val screenPoint: Vector2, val button: Int) : PlayerControlEvent()
    class CameraScaleInc: PlayerControlEvent()
    class CameraScaleDec: PlayerControlEvent()
    class CameraScrolled(val amount: Int): PlayerControlEvent()
    class CameraMoved(val amount: Vector2): PlayerControlEvent()
    class KeyPressed(val code: Int): PlayerControlEvent()
    class NumKeyPressed(val number: Int): PlayerControlEvent()
    class VoidClick(val button: Int, val point: GridPoint2) : PlayerControlEvent()

    //global
    open class GameEvent: Event()
    open class GlobalGameEvent: Event()
    open class GameStartedEvent(val player: PlayerInfo, val world: WorldInfo): GlobalGameEvent()
    open class GameFinishedEvent: GlobalGameEvent()
    open class WorldInitEvent(val world: WorldInfo) : GlobalGameEvent()
    open class TimeChanged(val turn: Long) : GameEvent()

    //debug
    open class DebugCombatEvent: Event()
    open class DebugToggleFocusEvent: Event()
    open class DebugShowPlayerInventoryEvent: Event()
    open class DebugShowInventoryEvent(val items: Collection<Entity>, entity: Entity): EntityEvent(entity)

    //level
    class LevelLoaded(val level: LevelInfo, val levelParams: LevelParams): GameEvent()

    //technical
    open class LogEvent(val message: String) : GameEvent()
    open class UpdateLightCollision(val points: Array<GridPoint2>) : GameEvent()
    open class QuestStatusUpdated(val quest: QuestInfo) : GameEvent()
    open class SkillSelected(val skill: SkillTemplate) : GameEvent()

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
    open class ShowEntityActionsEvent(entity: Entity, val interactions: Collection<EntityInteractionType>) : EntityEvent(entity)
    open class HideEntityActionsEvent() : GameEvent()
    open class ShowEntityContentEvent(entity: Entity) : EntityEvent(entity)
    open class EntityFovUpdated(entity: Entity) : EntityEvent(entity)
    //entity - view
    open class EntityAnimationStart(entity: Entity, val animation: AnimationType, val duration: Int) : EntityEvent(entity)
    open class EntityAnimationFinish(entity: Entity, val animation: AnimationType) : EntityEvent(entity)
    //entity - model
    open class EntityInteractionEvent(val source: Entity, target: Entity, val interaction: EntityInteraction) : EntityEvent(target)
    open class EntityUseSkill(source: Entity, val target: Any?, val skillTemplate: SkillTemplate) : EntityEvent(source)

    open class EntityChanged(entity: Entity) : EntityEvent(entity)
    open class EntityTakeDamage(entity: Entity, val source: Entity?, val amount: Int, val blockedAmount: Int) : EntityEvent(entity)
    open class EntityDied(entity: Entity) : EntityEvent(entity)
    open class EntityLevelTransition(entity: Entity, val connectionId: ConnectionId) : EntityEvent(entity)
    open class EntitySetMoveTarget(entity: Entity, val point: GridPoint2) : EntityEvent(entity)
    open class EntityStartMove(entity: Entity) : EntityEvent(entity)
    open class EntityMoved(entity: Entity) : EntityEvent(entity)
    open class EntityFinishMove(entity: Entity) : EntityEvent(entity)

    open class EntityContentInteraction(val source: Entity, target: Entity) : EntityEvent(target)
    open class EntityTakeItems(entity: Entity, val items: Collection<Entity>) : EntityEvent(entity)
}
