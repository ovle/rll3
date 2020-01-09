package com.ovle.rll3.view.layer

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.ovle.rll3.model.ecs.component.TilePosition


class CustomTiledMapTileLayer(width: Int, height: Int, tileWidth: Int, tileHeight: Int): TiledMapTileLayer(width, height, tileWidth, tileHeight) {

    private var bypassVisibilityFilter = true
    private var lastVisibleTiles: Set<TilePosition>? = null


    override fun getCell(x: Int, y: Int): Cell? {
        val cell = getCellIntr(x, y)
        val isVisible = cell?.tile.isVisible()

        return if (!isVisible && !bypassVisibilityFilter) null else cell
    }

    fun markVisiblePositions(visibleTiles: Set<TilePosition>) {
        lastVisibleTiles?.forEach {
            if (it !in visibleTiles) {
                setTileVisible(it.first, it.second, false)
            }
        }

        visibleTiles.forEach {(x, y) ->
            setTileVisible(x, y, true)
        }

        lastVisibleTiles = visibleTiles
    }

    private fun setTileVisible(x: Int, y: Int, visible: Boolean) {
        val cell = getCellIntr(x, y)
        val tile = cell?.tile
        tile?.setVisible(visible)
    }

    private fun getCellIntr(x: Int, y: Int): Cell? = super.getCell(x, y)
}