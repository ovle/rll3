package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId
import com.ovle.rll3.model.ecs.system.level.LevelId
import com.ovle.rll3.model.procedural.grid.processor.RoomInfo
import com.ovle.rll3.model.tile.TileArray
import java.util.*

data class LevelInfo(
    val id: LevelId = UUID.randomUUID(),
    val descriptionId: LevelDescriptionId,
    val tiles: TileArray
) {
    val rooms: MutableCollection<RoomInfo> = mutableListOf()
    val objects: MutableCollection<Entity> = mutableListOf()
}

class LevelComponent(var level: LevelInfo): Component