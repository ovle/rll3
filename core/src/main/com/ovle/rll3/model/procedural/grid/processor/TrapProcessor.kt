package com.ovle.rll3.model.procedural.grid.processor

//todo more complex trap types
//class TrapProcessor : TilesProcessor {
//
//    override fun process(tiles: TileArray, gameEngine: Engine): Collection<Entity> {
//        val result = mutableListOf<Entity>()
//        factoryParams as DungeonLevelFactoryParams
//
//        for (x in 0 until tiles.size) {
//            for (y in 0 until tiles.size) {
//                val nearTiles = nearValues(tiles, x, y)
//                val isRoomFloorTile = nearTiles.value?.typeId == roomFloorTileId
//                val isFreeForTrap = isRoomFloorTile
//                val isTrap = isFreeForTrap && Math.random() <= factoryParams.trapChance
//                if (isTrap) {
//                    result.add(newTrigger(point(x, y), gameEngine))
//                }
//            }
//        }
//        return result
//    }
//}