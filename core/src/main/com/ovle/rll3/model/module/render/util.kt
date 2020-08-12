package com.ovle.rll3.model.module.render

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.TextureRegions
import com.ovle.rll3.point
import com.ovle.rll3.roundToClosestByAbsInt
import com.ovle.rll3.view.sprite.Sprite
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth


fun Batch.draw(position: Vector2, region: TextureRegion, flipped: Boolean = false) {
    //store in render comp?
    val screenX = (position.x).roundToClosestByAbsInt() * tileWidth
    val screenY = (position.y).roundToClosestByAbsInt() * tileHeight

    val width = spriteWidth
    val x = screenX.toFloat()
    //use region.flip() ?
    draw(
            region,
            if (flipped) x + width else x,
            screenY.toFloat(),
            if (flipped) -width else width,
            spriteHeight
    )
}

val defaultSpritePoint = point(0, 0)

fun sprite(regions: TextureRegions, x: Int, y: Int): Sprite = Sprite(
        region = when {
            y >= regions.size || x >= regions[y].size
                -> regions[defaultSpritePoint.y][defaultSpritePoint.x]
            else -> regions[y][x] //switched
        }
)