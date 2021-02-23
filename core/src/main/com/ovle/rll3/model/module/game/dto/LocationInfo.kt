package com.ovle.rll3.model.module.game.dto

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.LocationId
import com.ovle.rlUtil.RandomParams
import com.ovle.rlUtil.TileArray
import com.ovle.rll3.model.procedural.grid.processor.location.room.RoomInfo
import com.ovle.rll3.model.procedural.grid.processor.location.structure.StructureTemplateInfo
import java.io.Serializable


data class LocationInfo(
    val id: LocationId,
    val locationPoint: GridPoint2,
    val random: RandomParams,
    val tiles: TileArray

//    val params: LocationGenerationParams,
//    val quests: MutableList<QuestInfo> = mutableListOf(),
//    val heightGrid: Grid,
//    val heatGrid: Grid
): Serializable {
    val areas: MutableCollection<AreaInfo> = mutableListOf()
    val rooms: MutableCollection<RoomInfo> = mutableListOf()
    val entities: MutableCollection<Entity> = mutableListOf()
    val structureTemplates: MutableCollection<StructureTemplateInfo> = mutableListOf()
}