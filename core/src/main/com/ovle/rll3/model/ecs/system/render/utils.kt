package com.ovle.rll3.model.ecs.system.render

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.roundToClosestByAbsInt
import com.ovle.rll3.view.layer.TextureRegions
import com.ovle.rll3.view.sprite.Sprite
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth


fun Batch.draw(position: Vector2, region: TextureRegion) {
    val screenX = (position.x).roundToClosestByAbsInt() * tileWidth
    val screenY = (position.y).roundToClosestByAbsInt() * tileHeight
    draw(
            region,
            screenX.toFloat(),
            screenY.toFloat(),
            spriteWidth.toFloat(),
            spriteHeight.toFloat()
    )
}

fun sprite(regions: TextureRegions, x: Int, y: Int): Sprite {
    return Sprite(
            region = regions[y][x] //switched
    )
}