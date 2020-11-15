package com.ovle.rll3

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.module.quest.QuestInfo
import com.ovle.rll3.model.util.Array2d
import com.ovle.rll3.model.tile.LightPassType
import com.ovle.rll3.model.tile.NearValues
import com.ovle.rll3.model.tile.TilePassType
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.view.layer.TileToTextureParams
import kotlin.random.Random

typealias SetScreen = (BaseScreen, Any?) -> Unit

typealias EntityFilter = (Entity) -> Boolean
typealias TaskPerformerFilter = EntityFilter
typealias TaskTargetFilter = (TaskTarget, LocationInfo) -> Boolean
typealias TaskAction = (Entity, TaskTarget) -> Unit
typealias SuccessCondition = (Entity, TaskTarget) -> Boolean
//typealias FailCondition = (Entity, TaskTarget) -> Boolean

typealias ComponentData = Map<String, Any?>
typealias ComponentFactory = (ComponentData?) -> Component

typealias EntityCheck = Entity.() -> Boolean

typealias EffectAmount = Int
typealias SkillEffect = (Entity, Any?, EffectAmount) -> Unit
typealias SkillSuccessCondition = (Entity, TaskTarget, LocationInfo) -> Boolean
typealias GetEffectAmount = (Entity) -> EffectAmount
typealias GetTarget = (GridPoint2, LocationInfo) -> Any?
typealias SkillCost = (Entity) -> Unit

typealias EntityId = String
typealias PlayerId = String
typealias WorldId = String
typealias LocationId = String
typealias LevelParamsId = String

typealias QuestCondition = () -> Boolean
typealias QuestHook = ((QuestInfo) -> Unit)?

typealias Seed = Long
typealias Turn = Long
typealias Ticks = Long
typealias ResourceAmount = Int
typealias ResourceGatherCost = Int
typealias RoomTiles = MutableList<Vector2>

typealias TileToTextureRegion = (TileToTextureParams) -> TextureRegion
typealias TextureRegions = Array<Array<TextureRegion>>
typealias MoveCostFn = (Tile, Tile?, TilePassTypeFn) -> Int
typealias MoveCostFn2 = (GridPoint2, GridPoint2?, TilePassTypeFn) -> Int
typealias IsPassableFn = (GridPoint2, TilePassTypeFn) -> Boolean
typealias GridValueCombinator = (Float, Float, Random) -> Float

typealias Tile = Char
typealias TileCondition = (Tile) -> Boolean
typealias TilePassTypeFn = (Tile) -> TilePassType
typealias LightPassTypeFn = (Tile) -> LightPassType
typealias TileArray = Array2d<Tile>
typealias NearTiles = NearValues<Tile?>
typealias TileMapper2 = (Float, Float) -> Tile
typealias TileMapper1 = (Float) -> Tile

typealias ValueCheck = (Float) -> Boolean
typealias GridPointCheck1 = (Grid, GridPoint2) -> Boolean
typealias GridPointCheck2 = (Grid, Grid, GridPoint2) -> Boolean

typealias MoveStrategy = (GridPoint2, GridPoint2, LocationInfo) -> Boolean
typealias IsAtPositionStrategy = (Entity, GridPoint2) -> Boolean