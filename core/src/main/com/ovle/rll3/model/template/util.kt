package com.ovle.rll3.model.template

fun parsedMask(mask: String?): List<List<Char>>? {
    return mask?.trim()
        ?.split("\n")
        ?.map {
            it.split(" ").map { chars -> chars[0] }
        }
}