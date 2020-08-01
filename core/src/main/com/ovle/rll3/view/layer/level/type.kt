package com.ovle.rll3.view.layer.level

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.tile.NearTiles
import com.ovle.rll3.view.defaultAnimationInterval
import com.ovle.rll3.view.layer.TextureRegionsInfo

data class TileToTextureParams(
    val nearTiles: NearTiles,
    val textureRegions: TextureRegionsInfo,
    val levelInfo: LevelInfo,
    val lightInfo: Map<GridPoint2, Double>
)

data class TileTextureInfo(
    val regions: Array<TextureRegion>,
    val animationInterval: Float = defaultAnimationInterval
)
