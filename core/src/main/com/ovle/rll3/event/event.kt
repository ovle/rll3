package com.ovle.rll3.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.Turn
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.ecs.component.dto.TaskTarget
import com.ovle.rll3.model.ecs.system.interaction.EntityInteractionType
import com.ovle.rll3.model.ecs.system.interaction.skill.SkillTemplate
import com.ovle.rll3.model.ecs.system.quest.QuestInfo
import com.ovle.rll3.model.procedural.config.LevelParams
import com.ovle.rll3.model.template.entity.view.AnimationType
import com.ovle.rll3.model.tile.Tile

sealed class Event {
    var next: Event? = null

    fun then(next: Event?) = this.apply { this@Event.next = next }

    open class PlayerControl : Event()
    class MouseMoved(val viewportPoint: Vector2) : PlayerControl()
    class MouseClick(val viewportPoint: Vector2, val button: Int) : PlayerControl()
    class CameraScaleInc: PlayerControl()
    class CameraScaleDec: PlayerControl()
    class CameraScrolled(val amount: Int): PlayerControl()
    class CameraMoved(val amount: Vector2): PlayerControl()
    class KeyPressed(val code: Int): PlayerControl()
    class NumKeyPressed(val number: Int): PlayerControl()
    class Click(val button: Int, val point: GridPoint2) : PlayerControl()
    class VoidClick(val button: Int, val point: GridPoint2) : PlayerControl()

    //global
    open class Game: Event()
    open class GlobalGame: Event()
    class GameStarted(): GlobalGame()
    class GameFinished: GlobalGame()
    class TimeChanged(val turn: Turn) : Game()

    //debug
    class DebugSaveGame: Event()
    class ExitGame: Event()
    class DebugCombat: Event()
    class DebugShowPlayerInventory: Event()
    class DebugSwitchSelectionMode: Event()
    class DebugSwitchControlMode: Event()
    class DebugChangeSelectedTiles: Event()
    class DebugTileChanged(val tile: Tile, val position: GridPoint2): Event()
    class DebugShowInventory(val items: Collection<Entity>, entity: Entity): EntityEvent(entity)

    //level
    class LevelLoaded(val level: LevelInfo, val levelParams: LevelParams): Game()

    //technical
    class Log(val message: String) : Game()
    class UpdateLightCollision(val points: Array<GridPoint2>) : Game()
    class QuestStatusUpdated(val quest: QuestInfo) : Game()
    class SkillSelected(val skill: SkillTemplate) : Game()

    open class CheckTask(val target: TaskTarget) : Game()

    //entity
    open class EntityEvent(val entity: Entity) : Game()
    //entity - technical
    class EntityClick(val button: Int, entity: Entity) : EntityEvent(entity)
    class EntityHover(entity: Entity) : EntityEvent(entity)
    class EntityUnhover : Game()

    class EntityInitialized(entity: Entity) : EntityEvent(entity)
    class EntitySelect(entity: Entity) : EntityEvent(entity)
    class EntityDeselect(entity: Entity) : EntityEvent(entity)
    class ShowEntityInfo(entity: Entity) : EntityEvent(entity)
    class HideEntityInfo() : Game()
    class ShowEntityActions(entity: Entity, val interactions: Collection<EntityInteractionType>) : EntityEvent(entity)
    class HideEntityActions() : Game()
    class ShowEntityContent(entity: Entity) : EntityEvent(entity)
    class EntityFovUpdated(entity: Entity) : EntityEvent(entity)
    class EntityFocus(entity: Entity) : EntityEvent(entity)
    //entity - view
    class EntityAnimationStart(entity: Entity, val animation: AnimationType, val duration: Int) : EntityEvent(entity)
    class EntityAnimationFinish(entity: Entity, val animation: AnimationType) : EntityEvent(entity)
    //entity - model
    class EntityInteraction(val source: Entity, target: Entity, val interaction: com.ovle.rll3.model.ecs.system.interaction.EntityInteraction) : EntityEvent(target)
    class EntityUseSkill(source: Entity, val target: Any?, val skillTemplate: SkillTemplate) : EntityEvent(source)

    class EntityChanged(entity: Entity) : EntityEvent(entity)
    class EntityTakeDamage(entity: Entity, val source: Entity?, val amount: Int) : EntityEvent(entity)
    class EntityDied(entity: Entity) : EntityEvent(entity)
    class EntitySetMoveTarget(entity: Entity, val point: GridPoint2) : EntityEvent(entity)
    class EntityStartMove(entity: Entity) : EntityEvent(entity)
    class EntityMoved(entity: Entity) : EntityEvent(entity)
    class EntityFinishMove(entity: Entity) : EntityEvent(entity)

    class EntityContentInteraction(val source: Entity, target: Entity) : EntityEvent(target)
    class EntityTakeItems(entity: Entity, val items: Collection<Entity>) : EntityEvent(entity)
}
