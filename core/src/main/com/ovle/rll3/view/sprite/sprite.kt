package com.ovle.rll3.view.sprite

import com.badlogic.gdx.graphics.g2d.TextureRegion

class Sprite(
    private val region: TextureRegion? = null
) {
    fun textureRegion() = region!!
}