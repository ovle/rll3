package com.ovle.rll3.model.template.structure

import com.ovle.rll3.model.template.SpawnTemplate
import com.ovle.rll3.model.template.parsedMask
import com.ovle.rll3.model.tile.TileType

data class StructureTemplates(val templates: Collection<StructureTemplate>)

data class StructureTemplate(
    var name: String = "",
    var version: String = "0.1",
    var description: String = "",
    var spawns: Collection<SpawnTemplate> = listOf(),
    var mask: String = ""
) {
    val parsedMask: List<List<TileType>>? by lazy {
        parsedMask(mask)
    }
}
