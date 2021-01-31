package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rlUtil.GridValueCombinator
import com.ovle.rlUtil.RandomParams
import com.ovle.rlUtil.noise4j.grid.factory.GridFactory


class Combine(val factory1: GridFactory, val factory2: GridFactory, val combinator: GridValueCombinator): GridFactory {

    override fun get(random: RandomParams): Grid {
        val grid1 = factory1.get(random)
        val grid2 = factory2.get(random)

        grid1.forEach {
            grid, x, y, value ->
            val newValue = combinator.invoke(value, grid2[x, y], random.kRandom)
            grid.set(x, y, newValue)

            Grid.CellConsumer.CONTINUE
        }

        return grid1
    }
}