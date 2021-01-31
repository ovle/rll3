package com.ovle.rll3.model.module.render

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.TextureRegions
import com.ovle.rlUtil.gdx.math.point
import com.ovle.rlUtil.roundToClosestByAbsInt
import com.ovle.rlUtil.gdx.math.vec2
import com.ovle.rll3.view.sprite.Sprite
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth
import ktx.math.vec2


fun Batch.draw(position: GridPoint2, renderComponent: RenderComponent) {
    val region = renderComponent.currentRegion() ?: return
    val animation = renderComponent.currentAnimation

    draw(vec2(position), region, renderComponent.flipped, animation)
}

fun Batch.draw(position: Vector2, region: TextureRegion, flipped: Boolean = false, animation: AnimationInfo? = null) {
    val screenX = (position.x).roundToClosestByAbsInt() * tileWidth
    val screenY = (position.y).roundToClosestByAbsInt() * tileHeight
    val width = spriteWidth
    //use region.flip() ?

    var r: TextureRegion? = region
    var p = vec2(screenX.toFloat(), screenY.toFloat())
    animation?.let {
        p = it.animation.process(p, animation)
        r = it.animation.process(r!!, animation)
    }

    if (r == null) return

    draw(
        r,
        if (flipped) p.x + width else p.x,
        p.y,
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