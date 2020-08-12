package com.ovle.rll3.model.template.structure

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.Tile
import com.ovle.rll3.model.template.entity.SpawnTemplate
import com.ovle.rll3.model.template.parsedMask


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
    val parsedMask: List<List<Tile>> by lazy {
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
