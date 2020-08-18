package com.ovle.rll3.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.Tile
import com.ovle.rll3.Turn
import com.ovle.rll3.model.module.game.LevelInfo
import com.ovle.rll3.model.module.interaction.ControlMode
import com.ovle.rll3.model.module.interaction.SelectionMode
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.module.skill.SkillTemplate
import com.ovle.rll3.model.module.quest.QuestInfo
import com.ovle.rll3.model.module.task.TaskInfo
import com.ovle.rll3.model.procedural.config.LevelParams
import com.ovle.rll3.model.template.entity.EntityTemplate

sealed class Event {
    var next: Event? = null

    fun then(next: Event?) = this.apply { this@Event.next = next }

    sealed class PlayerControlEvent : Event() {
        class MouseMovedEvent(val viewportPoint: Vector2) : PlayerControlEvent()
        class MouseClickEvent(val viewportPoint: Vector2, val button: Int) : PlayerControlEvent()
        class CameraScaleIncCommand : PlayerControlEvent()
        class CameraScaleDecCommand : PlayerControlEvent()
        class CameraScrollCommand(val amount: Int) : PlayerControlEvent()
        class KeyPressedEvent(val code: Int) : PlayerControlEvent()
        class NumKeyPressedEvent(val number: Int) : PlayerControlEvent()
        class ClickEvent(val button: Int, val point: GridPoint2) : PlayerControlEvent()
        class DragEvent(val start: Vector2, val current: Vector2, val lastDiff: Vector2) : PlayerControlEvent()
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

        //entity - technical
        class EntityClickEvent(val button: Int, val entity: Entity) : GameEvent()
        class EntityHoverEvent(val entity: Entity) : GameEvent()
        class DestroyEntityCommand(val entity: Entity) : GameEvent()
        class EntityInitializedEvent(val entity: Entity) : GameEvent()
        class EntityDestroyedEvent(val entity: Entity) : GameEvent()
        class ShowEntityInfoCommand(val entity: Entity) : GameEvent()
        class EntityFovUpdatedEvent(val entity: Entity) : GameEvent()
        class FocusEntityCommand(val entity: Entity) : GameEvent()

        //entity - model
        class EntityInteraction(val source: Entity, val target: Entity, val interaction: EntityInteraction) : GameEvent()
        class EntityUseSkill(val source: Entity, val target: Any?, val skillTemplate: SkillTemplate) : GameEvent()
        class EntityChangedEvent(val entity: Entity) : GameEvent()
        class EntityGatheredEvent(val entity: Entity, val source: Entity?, val amount: Int) : GameEvent()
        class EntityTakeDamageEvent(val entity: Entity, val source: Entity?, val amount: Int) : GameEvent()
        class EntityDiedEvent(val entity: Entity) : GameEvent()
        class EntityStartMoveCommand(val entity: Entity, val point: GridPoint2) : GameEvent()
        class EntityStartedMoveEvent(val entity: Entity) : GameEvent()
        class EntityMovedEvent(val entity: Entity) : GameEvent()
        class EntityFinishedMoveEvent(val entity: Entity) : GameEvent()
        class EntityContentInteraction(val source: Entity, val target: Entity) : GameEvent()
        class EntityTakeItems(val entity: Entity, val items: Collection<Entity>) : GameEvent()

        class TaskStartedEvent(val task: TaskInfo) : GameEvent()
        class TaskFinishedEvent(val task: TaskInfo) : GameEvent()
        class TaskSucceedCommand(val task: TaskInfo) : GameEvent()
        class TaskFailCommand(val task: TaskInfo) : GameEvent()
    }

    //debug
    class DebugSaveGame : Event()
    class ExitGame : Event()
    class DebugSwitchSelectionMode(val selectionMode: SelectionMode) : Event()
    class DebugSwitchControlMode(val controlMode: ControlMode) : Event()
    class DebugTileChanged(val tile: Tile, val position: GridPoint2) : Event()

}
