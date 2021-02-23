package com.ovle.rll3

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rlUtil.gdx.screen.BaseScreen
import com.ovle.rll3.model.module.ai.behavior.*
import com.ovle.rll3.model.module.game.dto.LocationInfo
import com.ovle.rll3.model.module.quest.dto.QuestInfo
import com.ovle.rll3.model.module.skill.CostStatus
import com.ovle.rll3.model.module.skill.dto.SkillUsage
import com.ovle.rll3.model.module.task.dto.TaskTarget

typealias SetScreen = (BaseScreen, Any?) -> Unit

typealias EntityFilter = (Entity) -> Boolean
typealias TaskPerformerFilter = (Entity, TaskTarget, LocationInfo) -> Boolean
typealias TaskTargetFilter = (TaskTarget, LocationInfo) -> Boolean
typealias TaskTargetMap = (TaskTarget, LocationInfo) -> Collection<TaskTarget>
typealias TaskAction = (Entity, TaskTarget) -> Unit
typealias SuccessCondition = (Entity, TaskTarget) -> Boolean
//typealias FailCondition = (Entity, TaskTarget) -> Boolean

typealias TaskExec = (TaskExecParams) -> TaskExecResult
typealias TaskExecFactory = (TaskTargetHolder) -> TaskExec

typealias ComponentData = Map<String, Any?>
typealias ComponentFactory = (ComponentData?) -> Component

typealias EntityCheck = Entity.() -> Boolean

typealias EffectAmount = Int
typealias SkillEffect = (SkillUsage, EffectAmount) -> Unit
typealias SkillSuccessCondition = (Entity, TaskTarget, LocationInfo) -> Boolean
typealias GetEffectAmount = (Entity) -> EffectAmount
typealias GetTarget = (GridPoint2, LocationInfo) -> Any?
typealias SkillCost = (Entity) -> CostStatus

typealias ProcessEffectCondition = (LocationInfo) -> Boolean
typealias ProcessEffect = (LocationInfo) -> Unit

typealias EntityId = String
typealias PlayerId = String
typealias WorldId = String
typealias LocationId = String

typealias QuestCondition = () -> Boolean
typealias QuestHook = ((QuestInfo) -> Unit)?

typealias Seed = Long
typealias Turn = Long
typealias ExactTurn = Double
typealias ResourceAmount = Int
typealias ResourceGatherCost = Int
typealias RoomTiles = MutableList<Vector2>

typealias MoveStrategy = (GridPoint2, GridPoint2, LocationInfo) -> Boolean
typealias BTFactory = (TaskTargetHolder) -> BehaviorTree<BTParams>
typealias BehaviorSelector = (Entity, LocationInfo) -> BTTemplate

typealias TemplatedState = Map<String, Any?>