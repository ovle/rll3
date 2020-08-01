package com.ovle.rll3.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.ecs.component.dto.PlayerInfo
import com.ovle.rll3.model.ecs.component.dto.WorldInfo
import com.ovle.rll3.model.ecs.system.interaction.EntityInteraction
import com.ovle.rll3.model.ecs.system.interaction.EntityInteractionType
import com.ovle.rll3.model.ecs.system.interaction.skill.SkillTemplate
import com.ovle.rll3.model.ecs.system.level.ConnectionId
import com.ovle.rll3.model.ecs.system.quest.QuestInfo
import com.ovle.rll3.model.procedural.config.LevelParams
import com.ovle.rll3.model.template.entity.view.AnimationType

sealed class Event() {
    var next: Event? = null

    fun then(next: Event?) = this.apply { this@Event.next = next }

    open class PlayerControlEvent : Event()
    class MouseMoved(val viewportPoint: Vector2) : PlayerControlEvent()
    class MouseClick(val viewportPoint: Vector2, val button: Int) : PlayerControlEvent()
    class CameraScaleInc: PlayerControlEvent()
    class CameraScaleDec: PlayerControlEvent()
    class CameraScrolled(val amount: Int): PlayerControlEvent()
    class CameraMoved(val amount: Vector2): PlayerControlEvent()
    class KeyPressed(val code: Int): PlayerControlEvent()
    class NumKeyPressed(val number: Int): PlayerControlEvent()
    class Click(val button: Int, val point: GridPoint2) : PlayerControlEvent()
    class VoidClick(val button: Int, val point: GridPoint2) : PlayerControlEvent()

    //global
    open class GameEvent: Event()
    open class GlobalGameEvent: Event()
    class GameStartedEvent(val player: PlayerInfo, val world: WorldInfo): GlobalGameEvent()
    class GameFinishedEvent: GlobalGameEvent()
    class WorldInitEvent(val world: WorldInfo) : GlobalGameEvent()
    class TimeChanged(val turn: Long) : GameEvent()

    //debug
    class DebugSaveGameEvent: Event()
    class ExitGameEvent: Event()
    class DebugCombatEvent: Event()
    class DebugToggleFocusEvent: Event()
    class DebugShowPlayerInventoryEvent: Event()
    class DebugShowInventoryEvent(val items: Collection<Entity>, entity: Entity): EntityEvent(entity)

    //level
    class LevelLoaded(val level: LevelInfo, val levelParams: LevelParams): GameEvent()

    //technical
    class LogEvent(val message: String) : GameEvent()
    class UpdateLightCollision(val points: Array<GridPoint2>) : GameEvent()
    class QuestStatusUpdated(val quest: QuestInfo) : GameEvent()
    class SkillSelected(val skill: SkillTemplate) : GameEvent()

    //entity
    open class EntityEvent(val entity: Entity) : GameEvent()
    //entity - technical
    class EntityClick(val button: Int, entity: Entity) : EntityEvent(entity)
    class EntityHoverEvent(entity: Entity) : EntityEvent(entity)
    class EntityUnhoverEvent : GameEvent()

    class EntityInitialized(entity: Entity) : EntityEvent(entity)
    class EntitySelectEvent(entity: Entity) : EntityEvent(entity)
    class EntityDeselectEvent(entity: Entity) : EntityEvent(entity)
    class ShowEntityInfoEvent(entity: Entity) : EntityEvent(entity)
    class HideEntityInfoEvent() : GameEvent()
    class ShowEntityActionsEvent(entity: Entity, val interactions: Collection<EntityInteractionType>) : EntityEvent(entity)
    class HideEntityActionsEvent() : GameEvent()
    class ShowEntityContentEvent(entity: Entity) : EntityEvent(entity)
    class EntityFovUpdated(entity: Entity) : EntityEvent(entity)
    //entity - view
    class EntityAnimationStart(entity: Entity, val animation: AnimationType, val duration: Int) : EntityEvent(entity)
    class EntityAnimationFinish(entity: Entity, val animation: AnimationType) : EntityEvent(entity)
    //entity - model
    class EntityInteractionEvent(val source: Entity, target: Entity, val interaction: EntityInteraction) : EntityEvent(target)
    class EntityUseSkill(source: Entity, val target: Any?, val skillTemplate: SkillTemplate) : EntityEvent(source)

    class EntityChanged(entity: Entity) : EntityEvent(entity)
    class EntityTakeDamage(entity: Entity, val source: Entity?, val amount: Int, val blockedAmount: Int) : EntityEvent(entity)
    class EntityDied(entity: Entity) : EntityEvent(entity)
    class EntityLevelTransition(entity: Entity, val connectionId: ConnectionId) : EntityEvent(entity)
    class EntitySetMoveTarget(entity: Entity, val point: GridPoint2) : EntityEvent(entity)
    class EntityStartMove(entity: Entity) : EntityEvent(entity)
    class EntityMoved(entity: Entity) : EntityEvent(entity)
    class EntityFinishMove(entity: Entity) : EntityEvent(entity)

    class EntityContentInteraction(val source: Entity, target: Entity) : EntityEvent(target)
    class EntityTakeItems(entity: Entity, val items: Collection<Entity>) : EntityEvent(entity)
}
