package com.ovle.rll3.model.tile

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.procedural.grid.processor.RoomInfo
import ktx.ashley.has
import kotlin.reflect.KClass

//todo entity?
data class LevelInfo(val tiles: TileArray) {
    val rooms: MutableCollection<RoomInfo> = mutableListOf()
    val objects: MutableCollection<Entity> = mutableListOf()

    fun entitiesWith(componentClass: KClass<out Component>) = ComponentMapper.getFor(componentClass.java)
        .run {
            objects.filter { it.has(this) }
        }
}
