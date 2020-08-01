package com.ovle.rll3.model.tile

typealias TileType = Char

typealias TilePassTypeFn = ((Tile) -> TilePassType)
typealias LightPassTypeFn = ((Tile) -> LightPassType)

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

