package com.ovle.rll3.view.layer

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.tile.NearTiles
import com.ovle.rll3.view.layer.TextureRegionsInfo

data class TileToTextureParams(
    val nearTiles: NearTiles,
    val textureRegions: TextureRegionsInfo,
    val levelInfo: LevelInfo,
    val lightInfo: Map<GridPoint2, Double>
)