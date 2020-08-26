package com.ovle.rll3.model.procedural.grid.generator

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.procedural.grid.util.normalize
import java.awt.Rectangle
import java.util.*


class FractalMapGenerator(
    var constantNoiseValue: Float = 0.0f, //noise value that doesn't depends on cell size
    var flexibleNoiseValue: Float = 2.0f, //noise value that depends on cell size
    var startIteration: Int = 3, //main generation parameter (low value = large clusters)
    var shouldRandomizeFinalIteration: Boolean = false,
    var shouldNormalize: Boolean = true,

    //for test only
    var stopIteration: Int = -1,
    var initialBorderValues: Array<FloatArray>? = null,
    var random: Random
) {

    companion object {
        private const val MIN_AREA_SIZE = 3
        private const val TILE_NOT_INITIALIZED_ID = 0.5f
    }

    private var areaLength = 0
    private val neighbourValues: MutableList<Float> = ArrayList()
    private val isToroidal: Boolean
        private get() = initialBorderValues == null


    //---------------------------------------------------------------------------------------------
    //  
    //---------------------------------------------------------------------------------------------

    fun generate(grid: Grid) {
        areaLength = grid.width

        fillArea(grid, TILE_NOT_INITIALIZED_ID)
        val usePreloadedValues = initialBorderValues != null
        if (usePreloadedValues) {
            initMapWithPreloadedValues(grid)
        } else {
            initMap(grid)
        }
        processArea(grid, 0, 0, grid.width, grid.height, startIteration)

        if (shouldNormalize) {
            normalize(grid)
        }
    }

    //---------------------------------------------------------------------------------------------
    //	Initialization
    //---------------------------------------------------------------------------------------------
    private fun initMap(grid: Grid) {
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

        grid[0, 0] = initialBorderValues!![0][0]
        grid[0, height] = initialBorderValues!![0][2]
        grid[width, 0] = initialBorderValues!![2][0]
        grid[width, height] = initialBorderValues!![2][2]
        grid[0, halfHeight] = initialBorderValues!![0][1]
        grid[halfWidth, 0] = initialBorderValues!![1][0]
        grid[width, halfHeight] = initialBorderValues!![2][1]
        grid[halfWidth, height] = initialBorderValues!![1][2]
        grid[halfWidth, halfHeight] = initialBorderValues!![1][1]
    }

    //---------------------------------------------------------------------------------------------
    //	Area initialization
    //---------------------------------------------------------------------------------------------
    private fun fillArea(grid: Grid, tileId: Float) {
        grid.fill(tileId)
    }

    private fun initArea(grid: Grid, actualArea: Rectangle) {
        val width = actualArea.width - 1
        val height = actualArea.height - 1
        val maxX = actualArea.x + width
        val maxY = actualArea.y + height
        var value = 0.0f

        if (!grid.isInitialized(actualArea.x, actualArea.y)) {
            value = random.nextFloat()
            grid[actualArea.x, actualArea.y] = value
            if (isToroidal) {
                if (actualArea.x == 0) {
                    grid[grid.width - 1, actualArea.y] = value
                }
                if (actualArea.y == 0) {
                    grid[actualArea.x, grid.height - 1] = value
                }
            }
        }
        if (!grid.isInitialized(maxX, actualArea.y)) {
            value = random.nextFloat()
            grid[maxX, actualArea.y] = value
            if (isToroidal) {
                if (actualArea.y == 0) {
                    grid[maxX, grid.height - 1] = value
                }
            }
        }
        if (!grid.isInitialized(actualArea.x, maxY)) {
            value = random.nextFloat()
            grid[actualArea.x, maxY] = value
            if (isToroidal) {
                if (actualArea.x == 0) {
                    grid[grid.width - 1, maxY] = value
                }
            }
        }
        if (!grid.isInitialized(maxX, maxY)) {
            value = random.nextFloat()
            grid[maxX, maxY] = value
        }
    }


    //---------------------------------------------------------------------------------------------
    //	Helpers
    //---------------------------------------------------------------------------------------------
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
        cellWidth: Int, cellHeight: Int
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
            val randomOffset = randomLimit.toFloat() / areaLength.toFloat()
            val shouldSelectRandomNeighbour = shouldRandomizeFinalIteration &&
                (cellWidth == MIN_AREA_SIZE || cellHeight == MIN_AREA_SIZE)
            var result = 0.0f
            if (shouldSelectRandomNeighbour) {
                val neighbourIndex: Int = random.nextInt(neighboursCount)
                result = neighbourValues[neighbourIndex]
            } else {
                //main part
                var totalValue = 0.0f
                for (i in 0 until neighboursCount) {
                    totalValue += neighbourValues[i]
                }
                val midValue = totalValue / neighboursCount
                result = midValue
            }
            result += randomOffset * flexibleNoiseValue

            grid[x, y] = result

            if (isToroidal) {
                if (x == 0) {
                    grid[grid.width - 1, y] = result
                }
                if (y == 0) {
                    grid[x, grid.height - 1] = result
                }
            }
        }
    }

    //---------------------------------------------------------------------------------------------
    //	Main algorithm method
    //---------------------------------------------------------------------------------------------
    private fun processArea(grid: Grid, startX: Int, startY: Int, cellWidth: Int, cellHeight: Int, startIteration: Int) {
        var currCellWidth = cellWidth
        var currCellHeight = cellHeight
        var shouldStop = false
        var iterationsCount = 0
        while (!shouldStop) {
            val minWidthReached = currCellWidth < MIN_AREA_SIZE
            val minHeightReached = currCellHeight < MIN_AREA_SIZE
            shouldStop = minWidthReached && minHeightReached
            if (shouldStop) {
                break
            }

            //test
            if (iterationsCount == stopIteration) {
                break
            }
            val cellsInGridRow = grid.width / (currCellWidth - 1)
            val cellsInGridColumn: Int = grid.height / (currCellHeight - 1)
            val useRandomFilling = iterationsCount < startIteration
            //TODO: save first cycle params to not calculate twice
            //calc square center point value
            for (i in 0 until cellsInGridRow) {
                for (j in 0 until cellsInGridColumn) {
                    val x = startX + (currCellWidth - 1) * i
                    val y = startY + (currCellHeight - 1) * j
                    val middleX = x + currCellWidth / 2
                    val middleY = y + currCellHeight / 2

                    //System.out.println("process middle point " + middleX + "," + middleY);
                    if (!useRandomFilling) {
                        //main option
                        setTileValue(grid, middleX, middleY, false, false, currCellWidth, currCellHeight)
                    } else {
                        initArea(grid, Rectangle(x, y, currCellWidth, currCellHeight))
                    }
                }
            }

            //calc borders middle points values
            for (i in 0 until cellsInGridRow) {
                for (j in 0 until cellsInGridColumn) {
                    val x = startX + (currCellWidth - 1) * i
                    val y = startY + (currCellHeight - 1) * j
                    val middleX = x + currCellWidth / 2
                    val middleY = y + currCellHeight / 2
                    val maxX = x + currCellWidth - 1
                    val maxY = y + currCellHeight - 1

                    //System.out.println("process square = " + new Rectangle(x, y, cellWidth, cellHeight));
                    if (!useRandomFilling) {
                        setTileValue(grid, middleX, y, false, true, currCellWidth, currCellHeight)
                        setTileValue(grid, middleX, maxY, false, true, currCellWidth, currCellHeight)
                        setTileValue(grid, x, middleY, true, false, currCellWidth, currCellHeight)
                        setTileValue(grid, maxX, middleY, true, false, currCellWidth, currCellHeight)
                    } else {
                        val cellInitWidth = currCellWidth / 2 + 1
                        val cellInitHeight = currCellHeight / 2 + 1
                        initArea(grid, Rectangle(x, y, cellInitWidth, cellInitHeight))
                        initArea(grid, Rectangle(middleX, y, cellInitWidth, cellInitHeight))
                        initArea(grid, Rectangle(x, middleY, cellInitWidth, cellInitHeight))
                        initArea(grid, Rectangle(middleX, middleY, cellInitWidth, cellInitHeight))
                    }
                }
            }
            iterationsCount++
            currCellWidth = currCellWidth / 2 + 1
            currCellHeight = currCellHeight / 2 + 1
        }
    }

    fun Grid.isPointValid(x: Int, y: Int) = x in (0 until width) && y in (0 until height)
    fun Grid.isInitialized(x: Int, y: Int) = this[x, y] != TILE_NOT_INITIALIZED_ID
}
