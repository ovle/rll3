package com.ovle.rll3.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.Tile
import com.ovle.rlUtil.event.Event
import com.ovle.rlUtil.gdx.screen.BaseScreen
import com.ovle.rll3.EffectAmount
import com.ovle.rll3.ExactTurn
import com.ovle.rll3.Turn
import com.ovle.rll3.model.module.ai.behavior.BTParams
import com.ovle.rll3.model.module.game.dto.LocationInfo
import com.ovle.rll3.model.module.interaction.ControlMode
import com.ovle.rll3.model.module.interaction.SelectionMode
import com.ovle.rll3.model.module.task.dto.TaskTarget
import com.ovle.rll3.model.module.quest.dto.QuestInfo
import com.ovle.rll3.model.module.skill.dto.SkillUsage
import com.ovle.rll3.model.module.task.dto.TaskInfo
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.screen.game.InitGameInfo
import com.ovle.rll3.screen.game.InitPlaygroundInfo


//global
sealed class GameEvent : Event()
class StartGameCommand(val gameInfo: InitGameInfo) : GameEvent()
class StartPlaygroundCommand(val playgroundInfo: InitPlaygroundInfo) : GameEvent()
class TurnChangedEvent(val turn: Turn, val deltaTurns: Turn) : GameEvent()
class TimeChangedEvent(val exactDeltaTurns: ExactTurn) : GameEvent()
class LocationLoadedEvent(val location: LocationInfo) : GameEvent()

//technical
class SwitchScreenCommand(val type: Class<out BaseScreen>, val payload: Any? = null) : GameEvent()
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

class BtFinishedEvent(val tree: BehaviorTree<BTParams>) : GameEvent()

//entity - model
class EntityUseSkillCommand(val info: SkillUsage) : GameEvent()
class EntityStartUseSkillEvent(val info: SkillUsage) : GameEvent()
class EntityFinishUseSkillEvent(val info: SkillUsage, val amount: EffectAmount) : GameEvent()

class EntityInteraction(val source: Entity, val target: Entity, val interaction: EntityInteraction) : GameEvent()
class EntityChangedEvent(val entity: Entity) : GameEvent()
class EntityGatheredEvent(val entity: Entity, val source: Entity?) : GameEvent()
class EntityTakeDamageEvent(val entity: Entity, val source: Entity?, val amount: Int) : GameEvent()
class EntityEatEvent(val entity: Entity, val food: Entity) : GameEvent()
class EntityDiedEvent(val entity: Entity) : GameEvent()
class EntityResurrectedEvent(val entity: Entity) : GameEvent()
class EntityStarvedEvent(val entity: Entity) : GameEvent()
class EntityStartMoveCommand(val entity: Entity, val point: GridPoint2) : GameEvent()
class EntityMoveCommand(val entity: Entity) : GameEvent()
class EntityStartedMoveEvent(val entity: Entity) : GameEvent()
class EntityMovedEvent(val entity: Entity) : GameEvent()
class EntityFinishedMoveEvent(val entity: Entity) : GameEvent()
class EntityContentInteraction(val source: Entity, val target: Entity) : GameEvent()

//        class EntityTakeItems(val entity: Entity, val items: Collection<Entity>) : GameEvent()
class EntityCarryItemEvent(val entity: Entity, val item: Entity) : GameEvent()
class EntityDropItemEvent(val entity: Entity, val item: Entity, val position: GridPoint2) : GameEvent()

class ChangeTileCommand(val tile: Tile, val position: GridPoint2) : GameEvent()
class TileChangedEvent(val tile: Tile, val position: GridPoint2) : Event()
class TileGatheredEvent(val tile: Tile, val position: GridPoint2) : Event()

class TaskStartedEvent(val task: TaskInfo) : GameEvent()
class TaskFinishedEvent(val task: TaskInfo) : GameEvent()
class TaskSucceedCommand(val task: TaskInfo) : GameEvent()
class TaskFailedCommand(val task: TaskInfo) : GameEvent()
class CancelAllTasksCommand() : GameEvent()


//debug
class DebugSaveGame : Event()
class ExitGameCommand : Event()
class GameDidFinishedEvent : Event()
class KillEntityCommand(val entity: Entity) : Event()
class ResurrectEntityCommand(val entity: Entity) : Event()
class DebugSwitchSelectionMode(val selectionMode: SelectionMode) : Event()
class DebugSwitchControlMode(val controlMode: ControlMode) : Event()
class IncGameSpeedCommand : Event()
class DecGameSpeedCommand : Event()
class GameSpeedChangedEvent(val speed: Double) : Event()
class SwitchPauseGameCommand : Event()
