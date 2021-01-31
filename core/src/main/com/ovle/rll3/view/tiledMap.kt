package com.ovle.rll3.view

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.utils.Array
import com.ovle.rlUtil.Tile
import com.ovle.rlUtil.TileArray
import com.ovle.rll3.TileToTextureRegion
import com.ovle.rlUtil.gdx.math.point
import com.ovle.rll3.view.layer.CustomTiledMapTileLayer
import com.ovle.rll3.view.layer.TextureRegionsInfo
import com.ovle.rll3.view.layer.TileTextureInfo
import com.ovle.rll3.view.layer.TileToTextureParams


fun tiledMap(tiles: TileArray, textureRegions: TextureRegionsInfo, tileToTextureRegion: TileToTextureRegion) =
    TiledMap().apply {
        layers.add(mapLayer(tiles, textureRegions, tileToTextureRegion))
    }

private fun mapLayer(tiles: TileArray, textureRegions: TextureRegionsInfo, tileToTextureRegion: TileToTextureRegion): MapLayer {
    val result = CustomTiledMapTileLayer(tiles.size, tiles.size, tileWidth, tileHeight)
    for (x in 0 until tiles.size) {
        for (y in 0 until tiles.size) {
            val tile = tiles[x, y]
            result.updateTile(tile, point(x, y), textureRegions, tileToTextureRegion)
        }
    }

    return result
}

fun TiledMapTileLayer.updateTile(tile: Tile, position: GridPoint2, textureRegions: TextureRegionsInfo, tileToTextureRegion: TileToTextureRegion) {
    val params = TileToTextureParams(tile, textureRegions)
    val tileTextureInfo = tileToTexture(params, tileToTextureRegion)
    val cell = textureToCell(tileTextureInfo)

    setCell(position.x, position.y, cell)
}

private fun tileToTexture(params: TileToTextureParams, tileToTextureRegion: TileToTextureRegion) =
    TileTextureInfo(
        regions = arrayOf(tileToTextureRegion(params))
    )

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