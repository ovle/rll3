package com.ovle.rll3.view.layer

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.view.defaultAnimationInterval

data class TileTextureInfo(
    val regions: Array<TextureRegion>,
    val animationInterval: Float = defaultAnimationInterval
)
