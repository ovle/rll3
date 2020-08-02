package com.ovle.rll3.model.tile

import com.ovle.rll3.TileType


data class Tile(
    var typeId: TileType = outOfMapTileId
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

