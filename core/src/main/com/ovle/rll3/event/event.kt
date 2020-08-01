package com.ovle.rll3.event

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.ecs.component.dto.PlayerInfo
import com.ovle.rll3.model.ecs.component.dto.WorldInfo
import com.ovle.rll3.model.ecs.system.interaction.EntityInteractionType
import com.ovle.rll3.model.ecs.system.interaction.skill.SkillTemplate
import com.ovle.rll3.model.ecs.system.level.ConnectionId
import com.ovle.rll3.model.ecs.system.quest.QuestInfo
import com.ovle.rll3.model.procedural.config.LevelParams
import com.ovle.rll3.model.template.entity.view.AnimationType

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
    class GameStarted(val player: PlayerInfo, val world: WorldInfo): GlobalGame()
    class GameFinished: GlobalGame()
    class WorldInit(val world: WorldInfo) : GlobalGame()
    class TimeChanged(val turn: Long) : Game()

    //debug
    class DebugSaveGame: Event()
    class ExitGame: Event()
    class DebugCombat: Event()
    class DebugToggleFocus: Event()
    class DebugShowPlayerInventory: Event()
    class DebugSwitchSelectionMode: Event()
    class DebugShowInventory(val items: Collection<com.badlogic.ashley.core.Entity>, entity: com.badlogic.ashley.core.Entity): Entity(entity)

    //level
    class LevelLoaded(val level: LevelInfo, val levelParams: LevelParams): Game()

    //technical
    class Log(val message: String) : Game()
    class UpdateLightCollision(val points: Array<GridPoint2>) : Game()
    class QuestStatusUpdated(val quest: QuestInfo) : Game()
    class SkillSelected(val skill: SkillTemplate) : Game()

    //entity
    open class Entity(val entity: com.badlogic.ashley.core.Entity) : Game()
    //entity - technical
    class EntityClick(val button: Int, entity: com.badlogic.ashley.core.Entity) : Entity(entity)
    class EntityHover(entity: com.badlogic.ashley.core.Entity) : Entity(entity)
    class EntityUnhover : Game()

    class EntityInitialized(entity: com.badlogic.ashley.core.Entity) : Entity(entity)
    class EntitySelect(entity: com.badlogic.ashley.core.Entity) : Entity(entity)
    class EntityDeselect(entity: com.badlogic.ashley.core.Entity) : Entity(entity)
    class ShowEntityInfo(entity: com.badlogic.ashley.core.Entity) : Entity(entity)
    class HideEntityInfo() : Game()
    class ShowEntityActions(entity: com.badlogic.ashley.core.Entity, val interactions: Collection<EntityInteractionType>) : Entity(entity)
    class HideEntityActions() : Game()
    class ShowEntityContent(entity: com.badlogic.ashley.core.Entity) : Entity(entity)
    class EntityFovUpdated(entity: com.badlogic.ashley.core.Entity) : Entity(entity)
    //entity - view
    class EntityAnimationStart(entity: com.badlogic.ashley.core.Entity, val animation: AnimationType, val duration: Int) : Entity(entity)
    class EntityAnimationFinish(entity: com.badlogic.ashley.core.Entity, val animation: AnimationType) : Entity(entity)
    //entity - model
    class EntityInteraction(val source: com.badlogic.ashley.core.Entity, target: com.badlogic.ashley.core.Entity, val interaction: com.ovle.rll3.model.ecs.system.interaction.EntityInteraction) : Entity(target)
    class EntityUseSkill(source: com.badlogic.ashley.core.Entity, val target: Any?, val skillTemplate: SkillTemplate) : Entity(source)

    class EntityChanged(entity: com.badlogic.ashley.core.Entity) : Entity(entity)
    class EntityTakeDamage(entity: com.badlogic.ashley.core.Entity, val source: com.badlogic.ashley.core.Entity?, val amount: Int, val blockedAmount: Int) : Entity(entity)
    class EntityDied(entity: com.badlogic.ashley.core.Entity) : Entity(entity)
    class EntityLevelTransition(entity: com.badlogic.ashley.core.Entity, val connectionId: ConnectionId) : Entity(entity)
    class EntitySetMoveTarget(entity: com.badlogic.ashley.core.Entity, val point: GridPoint2) : Entity(entity)
    class EntityStartMove(entity: com.badlogic.ashley.core.Entity) : Entity(entity)
    class EntityMoved(entity: com.badlogic.ashley.core.Entity) : Entity(entity)
    class EntityFinishMove(entity: com.badlogic.ashley.core.Entity) : Entity(entity)

    class EntityContentInteraction(val source: com.badlogic.ashley.core.Entity, target: com.badlogic.ashley.core.Entity) : Entity(target)
    class EntityTakeItems(entity: com.badlogic.ashley.core.Entity, val items: Collection<com.badlogic.ashley.core.Entity>) : Entity(entity)
}
