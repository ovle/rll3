package com.ovle.rll3.view.layer

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.utils.Array
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.component.lightByPosition
import com.ovle.rll3.model.ecs.component.lightTiles
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.view.defaultAnimationInterval
import com.ovle.rll3.view.layer.level.LayerType
import com.ovle.rll3.view.layer.level.TileToTextureParams
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth


fun mapLayer(levelInfo: LevelInfo, texturesInfo: TexturesInfo, layerType: LayerType, tileToTexture: (TileToTextureParams) -> kotlin.Array<TextureRegion>): MapLayer {
    val tiles = levelInfo.tiles
    val result = CustomTiledMapTileLayer(tiles.size, tiles.size, tileWidth, tileHeight)

    val textureRegions = TextureRegionsInfo(texturesInfo)
    val lightTiles = lightTiles(levelInfo)
    val lightInfo = lightByPosition(lightTiles)

    for (x in 0 until tiles.size) {
        for (y in 0 until tiles.size) {
            val nearTiles = nearValues(tiles, x, y)
            val params = TileToTextureParams(layerType, nearTiles, textureRegions, levelInfo, lightInfo)
            val tileTextureRegions = tileToTexture(params)
            val cell = textureToCell(tileTextureRegions)

            result.setCell(x, y, cell)
        }
    }

    return result
}

private fun textureToCell(tileTextureRegions: kotlin.Array<TextureRegion>): TiledMapTileLayer.Cell {
    val cell = TiledMapTileLayer.Cell()
    if (tileTextureRegions.isNotEmpty()) {
        val staticTiles = tileTextureRegions.map { StaticTiledMapTile(it) }.toTypedArray()
        cell.tile = if (staticTiles.size == 1) staticTiles.single()
            else AnimatedTiledMapTile(defaultAnimationInterval, Array(staticTiles))
    }
    return cell
}
