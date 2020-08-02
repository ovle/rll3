package com.ovle.rll3.view.layer

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.view.layer.TextureRegionsInfo

data class TileToTextureParams(
    val tile: Tile,
//    val nearTiles: NearTiles,
    val textureRegions: TextureRegionsInfo
)