package com.ovle.rll3.model.module.game

import com.badlogic.ashley.core.Entity
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.LocationId
import com.ovle.rll3.TileArray
import com.ovle.rll3.model.module.time.TimeInfo
import com.ovle.rll3.model.module.quest.QuestInfo
import com.ovle.rll3.model.procedural.config.LocationGenerationParams
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.grid.processor.room.RoomInfo
import com.ovle.rll3.model.procedural.grid.processor.structure.StructureTemplateInfo
import java.io.Serializable


data class LocationInfo(
    val id: LocationId,
    val random: RandomParams,
    val params: LocationGenerationParams,
    var time: TimeInfo,
    val quests: MutableList<QuestInfo> = mutableListOf(),
    val tiles: TileArray,

    val heightGrid: Grid,
    val heatGrid: Grid
): Serializable {
    val rooms: MutableCollection<RoomInfo> = mutableListOf()
    val entities: MutableCollection<Entity> = mutableListOf()
    val structureTemplates: MutableCollection<StructureTemplateInfo> = mutableListOf()
}