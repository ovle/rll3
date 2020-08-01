package com.ovle.rll3.model.ecs.component.dto

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.dto.LevelDescription
import com.ovle.rll3.model.ecs.system.level.LevelId
import com.ovle.rll3.model.procedural.grid.processor.RoomInfo
import com.ovle.rll3.model.procedural.grid.processor.StructureInfo
import com.ovle.rll3.model.tile.TileArray
import java.io.Serializable

data class LevelInfo(
    val id: LevelId,
    val description: LevelDescription,
    val tiles: TileArray
): Serializable {
    val rooms: MutableCollection<RoomInfo> = mutableListOf()
    val entities: MutableCollection<Entity> = mutableListOf()
    val structures: MutableCollection<StructureInfo> = mutableListOf()
}