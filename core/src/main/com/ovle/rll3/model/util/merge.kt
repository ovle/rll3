package com.ovle.rll3.model.util

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.procedural.grid.factory.CelullarAutomataGridFactory

//todo
//enum class MergeType {
//    FirstNotEmpty {
//        override fun apply(grids: Array<Grid>, x: Int, y: Int): Float? =
//            grids.map { it.get(x, y) }.find { it != CelullarAutomataGridFactory.deadCellMarker }
//    },
//    LastNotEmpty {
//        override fun apply(grids: Array<Grid>, x: Int, y: Int): Float? =
//            grids.map { it.get(x, y) }.findLast { it != CelullarAutomataGridFactory.deadCellMarker }
//    };
//
//    abstract fun apply(grids: Array<Grid>, x: Int, y: Int): Float?
//}
//
//private fun merge(grids: Array<Grid>, size: Int, mergeType: MergeType): Grid {
//    val result = Grid(size)
//    for (x in (0 until size)) {
//        for (y in (0 until size)) {
//            val value = mergeType.apply(grids, x, y) ?: continue
//            result.set(x, y, value)
//        }
//    }
//    return result
//}