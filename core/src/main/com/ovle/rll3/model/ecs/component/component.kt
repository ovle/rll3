package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.ovle.rll3.model.procedural.grid.processor.RoomInfo
import com.ovle.rll3.model.tile.TileArray


data class LevelInfo(val tiles: TileArray) {
    val rooms: MutableCollection<RoomInfo> = mutableListOf()
    val objects: MutableCollection<Entity> = mutableListOf()
}

class LevelComponent(var level: LevelInfo): Component

class PlayerControlledComponent : Component

class PositionComponent(var position: GridPoint2) : Component

class LightComponent(
    val radius: Int,
    val lightPositions: List<LightTilePosition> = listOf()
) : Component

class DoorComponent : Component

class TrapComponent : Component

class SightComponent(val radius: Int) : Component {
    var positions: Set<GridPoint2> = setOf()
}

class MoveComponent(val tilesPerSecond: Float = 2f, val path: MovePath = MovePath()) : Component

class SizeComponent(var size: Vector2) : Component

class RenderComponent(
        var sprite: SpriteDrawable? = null, //todo
        var visible: Boolean = true,
        val zLevel: Int = 0
) : Component

class AnimationComponent(
    var animation: Animation<TextureRegion> = NO_ANIMATION
) : Component {
    var time: Float = 0.0f
}