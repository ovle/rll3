package com.ovle.rll3.view.sprite

import com.badlogic.gdx.graphics.g2d.TextureRegion

class Sprite(
    private val region: TextureRegion? = null
) {

   var flipped: Boolean = false

    fun textureRegion() = region!!

    fun flip() {
        region?.flip(true, false)
//        animations?.values?.forEach { it.flip() }
//        if (offset != null) {
//            offset.x = -offset.x
//        }
    }
}