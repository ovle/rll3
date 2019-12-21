package com.ovle.rll3.model.tile

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.procedural.grid.RoomInfo
import ktx.ashley.has
import kotlin.reflect.KClass


data class LevelInfo(val tiles: TileArray) {
    val rooms: MutableCollection<RoomInfo> = mutableListOf()
    val objects: MutableCollection<Entity> = mutableListOf()

    fun <T: Component> entities(mapper: ComponentMapper<T>) = objects.filter { it.has(mapper) }

    //todo cache by position
    fun hasEntityOnPosition(x: Int, y: Int, kClass: KClass<out Component>): Boolean {
        return false
//        return entities().filter {  }
    }
}
