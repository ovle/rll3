package com.ovle.rll3.view.sprite

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth

//todo use config
fun sprite(spriteTexture: Texture, x: Int, y: Int): SpriteDrawable {
    val sprite = Sprite(
        spriteTexture,
        (spriteWidth * x).toInt(), (spriteHeight * y).toInt(),
        spriteWidth.toInt(), spriteHeight.toInt()
    )
    return SpriteDrawable(sprite)
}

//todo use config
fun sprite(entity: Entity, spriteTexture: Texture): SpriteDrawable {
    return sprite(spriteTexture, 0, 2)
}