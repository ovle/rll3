package com.ovle.rll3.model.ecs.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.componentMapper
import com.ovle.rll3.model.ecs.entitiesWith
import ktx.ashley.get

val NO_ANIMATION = Animation<TextureRegion>(0f)
val NO_TEXTURE = TextureRegion()

typealias TilePosition = Pair<Int, Int>


data class LightTilePosition(
    val value: Float,
    val tilePosition: TilePosition
)

class MovePath {
    private val path: MutableList<Vector2> = mutableListOf()

    private var currentIndex = -1
    private fun currentIndexValid() = currentIndex in (0 until path.size)

    fun currentTo() = if (currentIndexValid()) path[currentIndex] else null
    fun finished() = currentIndex >= path.size
    fun started() = currentIndex >= 0

    fun start(){
        check(!finished())
        currentIndex = 0
    }

    fun next() {
        check(!finished())
        currentIndex++
    }

    fun add(newTo: Vector2) {
        path.add(newTo)
    }

    fun set(newPath: List<Vector2>) {
        reset()
        path.addAll(newPath)
    }

    fun reset() {
        path.clear()
        currentIndex = -1
    }
}

//todo cache / memoize
fun lightTiles(levelInfo: LevelInfo): List<LightTilePosition> {
    val lightSources = entitiesWith(levelInfo.objects, LightComponent::class)
    val lightMapper = componentMapper<LightComponent>()
    return lightSources.map { it[lightMapper]!!.lightPositions }.flatten()
}

fun lightByPosition(lightTiles: List<LightTilePosition>) = lightTiles.groupBy { it.tilePosition }
    .mapValues { it.value.sumByDouble { lightTilePosition -> lightTilePosition.value.toDouble() } }