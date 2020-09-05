package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.isPointValid
import com.ovle.rll3.model.procedural.config.GridFactoryParams.FractalGridFactoryParams
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.grid.GridFactory
import kotlin.random.Random


class FractalGridFactory(val params: FractalGridFactoryParams): GridFactory {

    companion object {
        private const val MIN_AREA_SIZE = 3
        private const val TILE_NOT_INITIALIZED_ID = 0.5f
    }

    private val neighbourValues = mutableListOf<Float>()
    private val isToroidal: Boolean
        get() = params.initialBorderValues == null


    override fun get(random: RandomParams): Grid {
        val result = Grid(params.size)

        val r = random.kRandom
        result.fill(TILE_NOT_INITIALIZED_ID)
        val usePreloadedValues = params.initialBorderValues != null
        if (usePreloadedValues) {
            initMapWithPreloadedValues(result)
        } else {
            initMap(result, r)
        }

        processArea(result, r)

        return result
    }

    private fun initMap(grid: Grid, random: Random) {
        val width = grid.width - 1
        val height = grid.height - 1
        val value: Float = random.nextFloat()

        grid[0, 0] = value
        grid[width, 0] = value
        grid[0, height] = value
        grid[width, height] = value
    }

    private fun initMapWithPreloadedValues(grid: Grid) {
        val width = grid.width - 1
        val height: Int = grid.height - 1
        val halfWidth = width / 2
        val halfHeight = height / 2

        val initialBorderValues = params.initialBorderValues!!

        grid[0, 0] = initialBorderValues[0][0]
        grid[0, height] = initialBorderValues[0][2]
        grid[width, 0] = initialBorderValues[2][0]
        grid[width, height] = initialBorderValues[2][2]
        grid[0, halfHeight] = initialBorderValues[0][1]
        grid[halfWidth, 0] = initialBorderValues[1][0]
        grid[width, halfHeight] = initialBorderValues[2][1]
        grid[halfWidth, height] = initialBorderValues[1][2]
        grid[halfWidth, halfHeight] = initialBorderValues[1][1]
    }

    private fun processArea(grid: Grid, random: Random) {
        var currCellWidth = grid.width
        var currCellHeight = grid.height
        var shouldStop = false

        var iterationsCount = 0
        while (!shouldStop) {
            val minWidthReached = currCellWidth < MIN_AREA_SIZE
            val minHeightReached = currCellHeight < MIN_AREA_SIZE
            shouldStop = minWidthReached && minHeightReached
            if (shouldStop) break
            if (iterationsCount == params.stopIteration) break

            val cellsInGridRow = grid.width / (currCellWidth - 1)
            val cellsInGridColumn: Int = grid.height / (currCellHeight - 1)
            val useRandomFilling = iterationsCount < params.startIteration

            //calc square center point value
            for (i in 0 until cellsInGridRow) {
                for (j in 0 until cellsInGridColumn) {
                    val x = (currCellWidth - 1) * i
                    val y = (currCellHeight - 1) * j
                    val middleX = x + currCellWidth / 2
                    val middleY = y + currCellHeight / 2

                    //System.out.println("process middle point " + middleX + "," + middleY);
                    if (!useRandomFilling) {
                        setTileValue(grid, middleX, middleY, false, false, currCellWidth, currCellHeight, random)
                    } else {
                        initArea(grid, x, y, currCellWidth, currCellHeight, random)
                    }
                }
            }

            //calc borders middle points values
            for (i in 0 until cellsInGridRow) {
                for (j in 0 until cellsInGridColumn) {
                    val x = (currCellWidth - 1) * i
                    val y = (currCellHeight - 1) * j
                    val middleX = x + currCellWidth / 2
                    val middleY = y + currCellHeight / 2
                    val maxX = x + currCellWidth - 1
                    val maxY = y + currCellHeight - 1

                    //System.out.println("process square = " + new Rectangle(x, y, cellWidth, cellHeight));
                    if (!useRandomFilling) {
                        setTileValue(grid, middleX, y, false, true, currCellWidth, currCellHeight, random)
                        setTileValue(grid, middleX, maxY, false, true, currCellWidth, currCellHeight, random)
                        setTileValue(grid, x, middleY, true, false, currCellWidth, currCellHeight, random)
                        setTileValue(grid, maxX, middleY, true, false, currCellWidth, currCellHeight, random)
                    } else {
                        val cellInitWidth = currCellWidth / 2 + 1
                        val cellInitHeight = currCellHeight / 2 + 1

                        initArea(grid, x, y, cellInitWidth, cellInitHeight, random)
                        initArea(grid, middleX, y, cellInitWidth, cellInitHeight, random)
                        initArea(grid, x, middleY, cellInitWidth, cellInitHeight, random)
                        initArea(grid, middleX, middleY, cellInitWidth, cellInitHeight, random)
                    }
                }
            }
            iterationsCount++
            currCellWidth = currCellWidth / 2 + 1
            currCellHeight = currCellHeight / 2 + 1
        }
    }


    private fun initArea(grid: Grid, areaX: Int, areaY: Int, areaWidth: Int, areaHeight: Int, random: Random) {
        val width = areaWidth - 1
        val height = areaHeight - 1
        val maxX = areaX + width
        val maxY = areaY + height

        if (!grid.isInitialized(areaX, areaY)) {
            val value = random.nextFloat()
            grid[areaX, areaY] = value
            if (isToroidal) {
                if (areaX == 0) {
                    grid[grid.width - 1, areaY] = value
                }
                if (areaY == 0) {
                    grid[areaX, grid.height - 1] = value
                }
            }
        }
        if (!grid.isInitialized(maxX, areaY)) {
            val value = random.nextFloat()
            grid[maxX, areaY] = value
            if (isToroidal) {
                if (areaY == 0) {
                    grid[maxX, grid.height - 1] = value
                }
            }
        }
        if (!grid.isInitialized(areaX, maxY)) {
            val value = random.nextFloat()
            grid[areaX, maxY] = value
            if (isToroidal) {
                if (areaX == 0) {
                    grid[grid.width - 1, maxY] = value
                }
            }
        }
        if (!grid.isInitialized(maxX, maxY)) {
            grid[maxX, maxY] = random.nextFloat()
        }
    }

    private fun processNeighbour(grid: Grid, x: Int, y: Int) {
        if (grid.isPointValid(x, y)) {
            if (grid[x, y] != TILE_NOT_INITIALIZED_ID) {
                neighbourValues.add(grid[x, y])
            }
        }
    }

    private fun setTileValue(
        grid: Grid,
        x: Int, y: Int,
        isBorderX: Boolean, isBorderY: Boolean,
        cellWidth: Int, cellHeight: Int,
        random: Random
    ) {
        //corner points stay unaffected
        if (isBorderX && isBorderY) return
        //if already have value - return
        if (grid.isInitialized(x, y)) return

        val isCenterPoint = !isBorderX && !isBorderY
        val halfWidth = cellWidth / 2
        val halfHeight = cellHeight / 2
        val leftX = x - halfWidth
        val rightX = x + halfWidth
        val bottomY = y + halfHeight
        val topY = y - halfHeight

        neighbourValues.clear()

        if (isCenterPoint) {
            processNeighbour(grid, leftX, topY)
            processNeighbour(grid, leftX, bottomY)
            processNeighbour(grid, rightX, topY)
            processNeighbour(grid, rightX, bottomY)
        } else {
            processNeighbour(grid, x, topY)
            processNeighbour(grid, x, bottomY)
            processNeighbour(grid, leftX, y)
            processNeighbour(grid, rightX, y)
        }

        val neighboursCount = neighbourValues.size
        if (neighboursCount > 0) {
            val randomLimit: Int = random.nextInt(cellWidth) - cellWidth / 2
            val randomOffset = randomLimit.toFloat() / grid.width.toFloat()
            val shouldSelectRandomNeighbour = params.shouldRandomizeFinalIteration &&
                (cellWidth == MIN_AREA_SIZE || cellHeight == MIN_AREA_SIZE)

            val rawValue = rawTileValue(shouldSelectRandomNeighbour, neighboursCount, random)
            val value = rawValue + randomOffset * params.flexibleNoiseValue
            grid[x, y] = value

            if (isToroidal) {
                if (x == 0) grid[grid.width - 1, y] = value
                if (y == 0) grid[x, grid.height - 1] = value
            }
        }
    }

    private fun rawTileValue(shouldSelectRandomNeighbour: Boolean, neighboursCount: Int, random: Random): Float {
        return if (shouldSelectRandomNeighbour) {
            neighbourValues.random(random)
        } else {
            var totalValue = 0.0f
            for (i in 0 until neighboursCount) {
                totalValue += neighbourValues[i]
            }
            totalValue / neighboursCount
        }
    }

    private fun Grid.isInitialized(x: Int, y: Int) = this[x, y] != TILE_NOT_INITIALIZED_ID
}