package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.light.LightTilePosition
import com.ovle.rll3.model.ecs.component.move.MovePath
import com.ovle.rll3.model.procedural.grid.processor.RoomInfo
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.point
import com.ovle.rll3.view.sprite.Sprite
import com.ovle.rll3.view.sprite.animation.FrameAnimation


data class LevelInfo(val tiles: TileArray) {
    val rooms: MutableCollection<RoomInfo> = mutableListOf()
    val objects: MutableCollection<Entity> = mutableListOf()
}

class LevelComponent(var level: LevelInfo): Component

class PlayerControlledComponent : Component

class PositionComponent(var position: Vector2) : Component {
    val gridPosition: GridPoint2
        get() = point(position)
}

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
    var sprite: Sprite? = null, //todo
    var visible: Boolean = true,
    val zLevel: Int = 0
) : Component

class AnimationComponent(
    var animations: Map<String, FrameAnimation> = mapOf()
) : Component {
    var current: FrameAnimation? = null

    fun startAnimation(id: String) {
        current = animations[id]
        current?.start()
    }

    fun stopAnimation(id: String) {
        val animationToStop = animations[id]
        animationToStop?.stop()
        if (animationToStop == current) {
            current = null;
        }
    }
}