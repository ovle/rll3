package com.ovle.rll3.view.tiles

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.ovle.rll3.view.Palette

class Textures(
    val texture: Texture
) {
    val darkTexture = texture(texture, Palette::next)
    val lightTexture = texture(texture, Palette::prev)
}

private fun texture(texture: Texture, colorMapFunction: (Color) -> Color): Texture {
    val textureData = texture.textureData
    textureData.prepare()
    val sourcePm = textureData.consumePixmap()
    val width = sourcePm.width
    val height = sourcePm.height
    val destPm = Pixmap(width, height, Pixmap.Format.RGBA8888)
    for (x in 0..width) {
        for (y in 0..height) {
            val sourceValue = sourcePm.getPixel(x, y)   //RGBA8888
            val sourceColor = Color()
            Color.rgba8888ToColor(sourceColor, sourceValue)
            val destColor = colorMapFunction.invoke(sourceColor)
            destPm.drawPixel(x, y, Color.rgba8888(destColor))
        }
    }

    return Texture(destPm)
}