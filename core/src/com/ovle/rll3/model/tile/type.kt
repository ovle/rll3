package com.ovle.rll3.model.tile

typealias TileType = Int
typealias NearTiles = NearValues<Tile?>
typealias PassTypeFn = ((Tile) -> TilePassType)

data class Tile(
    val x: Int = -1,
    val y: Int = -1,
    val typeId: TileType = outOfMapTileId
)

enum class InfoDictionaryKey {
    Doors,
    Lights,
    Rooms,
    Exits
}

enum class TilePassType {
    Solid,
    Passable,
    Restricted,
}