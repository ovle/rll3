package com.ovle.rll3.view.layer

import com.ovle.rll3.Tile


data class TileToTextureParams(
    val tile: Tile,
//    val nearTiles: NearTiles,
    val textureRegions: TextureRegionsInfo
)