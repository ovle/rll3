package com.ovle.rll3.model.template

import com.ovle.rll3.model.tile.TileType

fun parsedMask(mask: String?): List<List<TileType>>? {
    return mask?.trim()
        ?.split("\n")
        ?.map {
            it.split(" ").map { chars -> chars[0] }
        }
}