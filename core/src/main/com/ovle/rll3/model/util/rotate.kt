package com.ovle.rll3.model.util


fun <T> rotate90(list: List<List<T>>): List<List<T>> = list.indices.map {
    x -> list[x].indices.map { y -> list[y][x] }
}

fun <T> rotate180(list: List<List<T>>) =
    list.map { it.reversed() }.reversed()

fun <T> rotate270(list: List<List<T>>) = rotate90(rotate180(list))