package com.ovle.rll3

import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.view.RenderConfig
import com.ovle.rll3.view.screenHeight
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth
import kotlin.math.roundToInt

fun IntRange.random() = (Math.random() * (this.last - this.first)).roundToInt() + this.first
fun <T> List<T>.random(): T? = if (this.isEmpty()) null else this[(0 until size).random()]
fun Int.withChance(chance: Float): Int = if (Math.random() <= chance) this else 0

operator fun Vector2.component1() = x
operator fun Vector2.component2() = y


fun toGamePoint(screenPoint: Vector2, renderConfig: RenderConfig): Vector2 {
    println("screenPoint $screenPoint")

    val mapScreenPoint = toMapScreenPoint(screenPoint, renderConfig)

    val scale = renderConfig.scale
    val offset = renderConfig.scrollOffset

//    val x = (screenPoint.x ) / (tileWidth * scale)
//    val y = (screenHeight - screenPoint.y ) / (tileHeight * scale)
         val x = (mapScreenPoint.x /*- screenWidth / 2*/) / tileWidth
        val y = (-mapScreenPoint.y + screenHeight) / tileHeight

    return Vector2(x, y)
}

fun toMapScreenPoint(screenPoint: Vector2, renderConfig: RenderConfig): Vector2 {
    //todo
    return screenPoint
}

//fun toScreenPoint(gamePoint: Vector2, renderConfig: RenderConfig): Vector2 {
////        val playerPosition = game.player.position
//    val scale = renderConfig.scale
//    val offset = renderConfig.scrollOffset
//
//    val screenWidth = Gdx.graphics.width.toFloat()
//    val screenHeight = Gdx.graphics.height.toFloat()
//    val screenX = (gamePoint.x + offset.x) * scale + screenWidth
//    val screenY = (gamePoint.y + offset.y) * scale + screenHeight
////        val screenX = (gamePoint.x - playerPosition.x) * scale + screenWidth * PLAYER_SCREEN_WIDTH_OFFSET_RATIO
////        val screenY = (gamePoint.y - playerPosition.y) * scale + screenHeight * PLAYER_SCREEN_HEIGHT_OFFSET_RATIO
//
//    return Vector2(screenX, screenY)// - scrollOffset
//}


fun isNearHV(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
    return x1 == x2 && y1 in ((y2 - 1)..(y2 + 1))
        || y1 == y2 && x1 in ((x2 - 1)..(x2 + 1))
}

fun isNear(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
    return (x1 in ((x2 - 1)..(x2 + 1))) && (y1 in ((y2 - 1)..(y2 + 1)))
}