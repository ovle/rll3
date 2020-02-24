package com.ovle.rll3.view.layer

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.view.noVisibilityFilter


class CustomTiledMapTileLayer(width: Int, height: Int, tileWidth: Int, tileHeight: Int): TiledMapTileLayer(width, height, tileWidth, tileHeight) {

    private var lastVisibleTiles: Set<GridPoint2>? = null


    override fun getCell(x: Int, y: Int): Cell? {
        val cell = getCellIntr(x, y)
        val isVisible = cell?.tile.isVisible()

        return if (!isVisible && !noVisibilityFilter) null else cell
    }

    fun markVisiblePositions(visibleTiles: Set<GridPoint2>) {
        lastVisibleTiles?.forEach {
            if (it !in visibleTiles) {
                setTileVisible(it.x, it.y, false)
            }
        }

        visibleTiles.forEach {
            setTileVisible(it.x, it.y, true)
        }

        lastVisibleTiles = visibleTiles
    }

    private fun setTileVisible(x: Int, y: Int, visible: Boolean) {
        val cell = getCellIntr(x, y)
        val tile = cell?.tile
        tile?.setVisible(visible)
    }

    private fun getCellIntr(x: Int, y: Int): Cell? = super.getCell(x, y)

    private fun TiledMapTile?.isVisible() = (this?.properties?.get("visible") as Boolean?) ?: false

    private fun TiledMapTile?.setVisible(value: Boolean?) {
        this?.properties?.put("visible", value)
    }
}