package com.ovle.rll3

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.ecs.component.dto.TaskTarget
import com.ovle.rll3.model.ecs.system.quest.QuestInfo
import com.ovle.rll3.model.tile.LightPassType
import com.ovle.rll3.model.tile.NearValues
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TilePassType

typealias EntityFilter = (Entity) -> Boolean
typealias TaskPerformerFilter = EntityFilter
typealias TaskAction = (Entity, TaskTarget) -> Unit
typealias SuccessCondition = (Entity, TaskTarget) -> Boolean
typealias FailCondition = (Entity, TaskTarget) -> Boolean

typealias ComponentData = Map<String, Any?>
typealias ComponentFactory = (ComponentData?) -> Component

typealias EntityCheck = Entity.() -> Boolean

typealias SkillEffect = (Entity, Any?) -> Unit
typealias GetTarget = (GridPoint2, LevelInfo) -> Any?
typealias SkillCost = (Entity) -> Unit

typealias EntityId = String
typealias PlayerId = String
typealias WorldId = String
typealias LevelId = String
typealias LevelDescriptionId = String
typealias LevelParamsId = String
typealias ConnectionId = String

typealias QuestCondition = (() -> Boolean)
typealias QuestHook = ((QuestInfo) -> Unit)?

typealias Turn = Long
typealias Ticks = Long
typealias Area = List<GridPoint2>
typealias RoomTiles = MutableList<Vector2>

typealias TextureRegions = Array<Array<TextureRegion>>
typealias MoveCostFn = ((Tile, Tile?, TilePassTypeFn) -> Int)
typealias MoveCostFn2 = ((GridPoint2, GridPoint2?, TilePassTypeFn) -> Int)
typealias IsPassableFn = ((GridPoint2, TilePassTypeFn) -> Boolean)

typealias TileType = Char
typealias TilePassTypeFn = ((Tile) -> TilePassType)
typealias LightPassTypeFn = ((Tile) -> LightPassType)

typealias NearTiles = NearValues<Tile?>