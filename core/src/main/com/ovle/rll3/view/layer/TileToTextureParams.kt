package com.ovle.rll3.view.layer

import com.ovle.rlUtil.Tile


data class TileToTextureParams(
    val tile: Tile,
//    val nearTiles: NearTiles,
    val textureRegions: TextureRegionsInfo
)