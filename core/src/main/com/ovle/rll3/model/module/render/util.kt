package com.ovle.rll3.model.module.render

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.gdx.math.vec2
import com.ovle.rlUtil.gdx.view.*
import com.ovle.rll3.view.spriteSize
import com.ovle.rll3.view.tileSize


fun Batch.draw(position: GridPoint2, renderComponent: RenderComponent) {
    val region = renderComponent.currentRegion() ?: return

    val animation = renderComponent.currentAnimation
    draw(
        position = vec2(position),
        region = region,
        flipped = renderComponent.flipped,
        animation = animation,
        tileSize = tileSize,
        spriteSize = spriteSize
    )
}

fun Batch.draw(position: GridPoint2, region: TextureRegion) {
    draw(
        position = vec2(position),
        region = region,
        flipped = false,
        animation = null,
        tileSize = tileSize,
        spriteSize = spriteSize
    )
}