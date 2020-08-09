package com.ovle.rll3.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.Turn
import com.ovle.rll3.model.module.game.LevelInfo
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.module.skill.SkillTemplate
import com.ovle.rll3.model.module.quest.QuestInfo
import com.ovle.rll3.model.procedural.config.LevelParams
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.model.tile.Tile

sealed class Event {
    var next: Event? = null

    fun then(next: Event?) = this.apply { this@Event.next = next }

    sealed class PlayerControlEvent : Event() {
        class MouseMovedEvent(val viewportPoint: Vector2) : PlayerControlEvent()
        class MouseClickEvent(val viewportPoint: Vector2, val button: Int) : PlayerControlEvent()
        class CameraScaleIncEvent : PlayerControlEvent()
        class CameraScaleDecEvent : PlayerControlEvent()
        class CameraScrolledEvent(val amount: Int) : PlayerControlEvent()
        class CameraMovedEvent(val amount: Vector2) : PlayerControlEvent()
        class KeyPressedEvent(val code: Int) : PlayerControlEvent()
        class NumKeyPressedEvent(val number: Int) : PlayerControlEvent()
        class ClickEvent(val button: Int, val point: GridPoint2) : PlayerControlEvent()
        class VoidClickEvent(val button: Int, val point: GridPoint2) : PlayerControlEvent()
    }

    //global
    sealed class GameEvent : Event() {
        class StartGameCommand : GameEvent()
        class TimeChangedEvent(val turn: Turn) : GameEvent()
        class LevelLoadedEvent(val level: LevelInfo, val levelParams: LevelParams) : GameEvent()

        //technical
        class LogCommand(val message: String) : GameEvent()
        class UpdateLightCollisionCommand(val points: Array<GridPoint2>) : GameEvent()
        class QuestStatusUpdatedEvent(val quest: QuestInfo) : GameEvent()
        class CheckTaskCommand(val target: TaskTarget) : GameEvent()
        class EntityUnhoverEvent : GameEvent()
        class CreateEntityCommand(val entityTemplate: EntityTemplate, val position: GridPoint2) : GameEvent()
        class HideEntityInfoCommand : GameEvent()

        sealed class EntityEvent(val entity: Entity) : GameEvent() {
            //entity - technical
            class EntityClickEvent(val button: Int, entity: Entity) : EntityEvent(entity)
            class EntityHoverEvent(entity: Entity) : EntityEvent(entity)
            class DestroyEntityCommand(entity: Entity) : EntityEvent(entity)
            class EntityInitializedEvent(entity: Entity) : EntityEvent(entity)
            class EntityDestroyedEvent(entity: Entity) : EntityEvent(entity)
            class ShowEntityInfoCommand(entity: Entity) : EntityEvent(entity)
            class EntityFovUpdatedEvent(entity: Entity) : EntityEvent(entity)
            class FocusEntityCommand(entity: Entity) : EntityEvent(entity)

            //entity - model
            class EntityInteraction(val source: Entity, target: Entity, val interaction: EntityInteraction) : EntityEvent(target)
            class EntityUseSkill(source: Entity, val target: Any?, val skillTemplate: SkillTemplate) : EntityEvent(source)
            class EntityChangedEvent(entity: Entity) : EntityEvent(entity)
            class EntityGatheredEvent(entity: Entity, val source: Entity?, val amount: Int) : EntityEvent(entity)
            class EntityTakeDamageEvent(entity: Entity, val source: Entity?, val amount: Int) : EntityEvent(entity)
            class EntityDiedEvent(entity: Entity) : EntityEvent(entity)
            class EntityStartMoveCommand(entity: Entity, val point: GridPoint2) : EntityEvent(entity)
            class EntityStartedMoveEvent(entity: Entity) : EntityEvent(entity)
            class EntityMovedEvent(entity: Entity) : EntityEvent(entity)
            class EntityFinishedMoveEvent(entity: Entity) : EntityEvent(entity)
            class EntityContentInteraction(val source: Entity, target: Entity) : EntityEvent(target)
            class EntityTakeItems(entity: Entity, val items: Collection<Entity>) : EntityEvent(entity)
        }
    }

    //debug
    class DebugSaveGame : Event()
    class ExitGame : Event()
    class DebugCombat : Event()
    class DebugShowPlayerInventory : Event()
    class DebugSwitchSelectionMode : Event()
    class DebugSwitchControlMode : Event()
    class DebugChangeSelectedTiles : Event()
    class DebugTileChanged(val tile: Tile, val position: GridPoint2) : Event()

}
