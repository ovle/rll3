package com.ovle.rll3.view.layer

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion.split
import com.ovle.rll3.view.palette.Palette
import com.ovle.rll3.view.textureTileHeight
import com.ovle.rll3.view.textureTileWidth

typealias TextureRegions = Array<Array<TextureRegion>>

class TexturesInfo(
    initialTexture: Texture
) {
    val texture: Texture = texture(initialTexture, Palette::map)
    val darkTexture = texture(texture, Palette::prev)
    val lightTexture = texture(texture, Palette::next)

    val all = arrayOf(texture, darkTexture, lightTexture)
}

class TextureRegionsInfo(
    texturesInfo: TexturesInfo
) {
    val regions = split(texturesInfo.texture, textureTileWidth, textureTileHeight)
    val darkRegions = split(texturesInfo.darkTexture, textureTileWidth, textureTileHeight)
    val lightRegions = split(texturesInfo.lightTexture, textureTileWidth, textureTileHeight)
}

private fun texture(texture: Texture, colorMapFunction: (Color) -> Color): Texture {
    val textureData = texture.textureData
    if (!textureData.isPrepared) textureData.prepare()

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