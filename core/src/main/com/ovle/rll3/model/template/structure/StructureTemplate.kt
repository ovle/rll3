package com.ovle.rll3.model.template.structure

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.template.SpawnTemplate
import com.ovle.rll3.model.template.parsedMask
import com.ovle.rll3.model.tile.TileType


data class StructureTemplate(
    var name: String = "",
    var version: String = "0.1",
    var description: String = "",
    var spawns: Collection<SpawnTemplate> = listOf(),
    var mask: String = "",
    var entities: Collection<StructureEntity> = listOf(),
    var playerSpawns: Collection<GridPoint2> = listOf(),
    var quests: Collection<StructureQuest> = listOf()
) {
    val parsedMask: List<List<TileType>> by lazy {
        parsedMask(mask)!!
    }
}

data class StructureEntity(
    var templateName: String = "",
    var points: Array<Pair<Int, Int>> = arrayOf(),
    var ids: Array<String> = arrayOf()
)

data class StructureQuest(
    var questId: String = "",
    var entityId: String = ""
)
