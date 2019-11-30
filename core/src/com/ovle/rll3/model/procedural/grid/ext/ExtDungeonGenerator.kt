package com.ovle.rll3.model.procedural.grid.ext

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator

//todo remove
class ExtDungeonGenerator: DungeonGenerator() {
    private lateinit var _rooms: List<Room>

    override fun joinRegions(grid: Grid?) {
        super.joinRegions(grid)

        val field = DungeonGenerator::class.java.declaredFields.find { it.name == "rooms" }!!
        field.trySetAccessible()
        _rooms = (field.get(this) as List<Room>).toList()
    }

    fun rooms() = _rooms
}