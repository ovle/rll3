package com.ovle.rll3.model.template

import com.ovle.rll3.TileType


fun parsedMask(mask: String?): List<List<TileType>>? {
    return mask?.trim()
        ?.split("\n")
        ?.map {
            it.trim().split(" ").map { chars -> chars[0] }
        }
}