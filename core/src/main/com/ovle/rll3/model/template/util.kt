package com.ovle.rll3.model.template

import com.ovle.rll3.Tile


fun parsedMask(mask: String?): List<List<Tile>>? {
    return mask?.trim()
        ?.split("\n")
        ?.map {
            it.trim().split(" ").map { chars -> chars[0] }
        }
}