package com.ovle.rll3

import RenderConfig
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth
import kotlin.math.roundToInt

fun IntRange.random() = (Math.random() * (this.last - this.first)).roundToInt() + this.first
fun <T> List<T>.random(): T? = if (this.isEmpty()) null else this[(0 until size).random()]
fun Int.withChance(chance: Float): Int = if (Math.random() <= chance) this else 0

operator fun Vector2.component1() = x
operator fun Vector2.component2() = y

fun toGamePoint(screenPoint: Vector2, renderConfig: RenderConfig): Vector2 {
//    val screenWidth = Gdx.graphics.width.toFloat()
    val screenHeight = Gdx.graphics.height.toFloat()

    val scale = renderConfig.scale
    val offset = renderConfig.scrollOffset
    val x = (screenPoint.x) / (tileWidth * scale) - offset.x
    val y = (screenHeight - screenPoint.y) / (tileHeight * scale) - offset.y
//         val x = (screenPosition.x - screenWidth * 0)/ (tileWidth * tileMapScale) + playerPosition.x
//        val y = (- screenPosition.y + screenHeight * (1 - 0))/ (tileHeight * tileMapScale) + playerPosition.y

    val result = Vector2(x, y)
//    println(result)
    return result
}

fun toScreenPoint(gamePoint: Vector2, renderConfig: RenderConfig): Vector2 {
//        val playerPosition = game.player.position
    val scale = renderConfig.scale
    val offset = renderConfig.scrollOffset

    val screenWidth = Gdx.graphics.width.toFloat()
    val screenHeight = Gdx.graphics.height.toFloat()
    val screenX = (gamePoint.x + offset.x) * scale + screenWidth
    val screenY = (gamePoint.y + offset.y) * scale + screenHeight
//        val screenX = (gamePoint.x - playerPosition.x) * scale + screenWidth * PLAYER_SCREEN_WIDTH_OFFSET_RATIO
//        val screenY = (gamePoint.y - playerPosition.y) * scale + screenHeight * PLAYER_SCREEN_HEIGHT_OFFSET_RATIO

    return Vector2(screenX, screenY)// - scrollOffset
}

fun isNearHV(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
    return x1 == x2 && y1 in ((y2 - 1)..(y2 + 1))
        || y1 == y2 && x1 in ((x2 - 1)..(x2 + 1))
}

fun isNear(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
    return (x1 in ((x2 - 1)..(x2 + 1))) && (y1 in ((y2 - 1)..(y2 + 1)))
}