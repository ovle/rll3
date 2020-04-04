package com.ovle.rll3.model.template

import com.ovle.rll3.model.tile.TileType

data class SpawnTemplate(
    var chance: Float = 1.0f,
    var mask: String? = null,
    var rotate: Boolean = false
) {
    val parsedMask: List<List<TileType>>? by lazy {
        mask?.trim()
            ?.split("\n")
            ?.map {
                it.split(" ").map { chars -> chars[0] }
            }
    }
}