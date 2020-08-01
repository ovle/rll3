package com.ovle.rll3.view.layer

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.utils.Array
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.model.util.lightByPosition
import com.ovle.rll3.model.util.lightTiles
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth


fun mapLayer(levelInfo: LevelInfo, texturesInfo: TexturesInfo, tileToTexture: (TileToTextureParams) -> TileTextureInfo): MapLayer {
    val tiles = levelInfo.tiles
    val result = CustomTiledMapTileLayer(tiles.size, tiles.size, tileWidth, tileHeight)

    val textureRegions = TextureRegionsInfo(texturesInfo)
    val lightTiles = lightTiles(levelInfo)
    val lightInfo = lightByPosition(lightTiles)

    for (x in 0 until tiles.size) {
        for (y in 0 until tiles.size) {
            val nearTiles = nearValues(tiles, x, y)
            val params = TileToTextureParams(nearTiles, textureRegions, levelInfo, lightInfo)
            val tileTextureInfo = tileToTexture(params)
            val cell = textureToCell(tileTextureInfo)

            result.setCell(x, y, cell)
        }
    }

    return result
}

private fun textureToCell(tileTextureInfo: TileTextureInfo): TiledMapTileLayer.Cell {
    val cell = TiledMapTileLayer.Cell()
    val regions = tileTextureInfo.regions
    if (regions.isNotEmpty()) {
        val staticTiles = regions.map { StaticTiledMapTile(it) }.toTypedArray()
        cell.tile = if (staticTiles.size == 1) staticTiles.single()
            else AnimatedTiledMapTile(tileTextureInfo.animationInterval, Array(staticTiles))
    }
    return cell
}
