package com.ovle.rll3.model.template.entity

import com.ovle.rll3.TileType
import com.ovle.rll3.model.template.parsedMask

data class SpawnTemplate(
    var chance: Float = 1.0f,
    var mask: String = """
        ` ` `
        ` _ `
        ` ` `
    """,
    var rotate: Boolean = false,
    var groupBonusChance: Float = 0.0f,
    var groupRadius: Int = 1
) {
    val parsedMask: List<List<TileType>>? by lazy {
        parsedMask(mask)
    }
}