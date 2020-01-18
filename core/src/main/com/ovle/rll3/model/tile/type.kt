package com.ovle.rll3.model.tile

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.point

typealias TileType = Int
typealias NearTiles = NearValues<Tile?>
typealias TilePassTypeFn = ((Tile) -> TilePassType)
typealias LightPassTypeFn = ((Tile) -> LightPassType)

data class Tile(
    val position: GridPoint2 = point(),
    val typeId: TileType = outOfMapTileId
)

enum class TilePassType {
    Solid,
    Passable,
    Restricted,
}

enum class LightPassType {
    Solid,
    Passable
}

enum class LightValueType {
    Full,
    Half,
    No
}