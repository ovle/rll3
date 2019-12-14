package com.ovle.rll3.view.tiles

import com.badlogic.gdx.maps.tiled.TiledMapTile


fun TiledMapTile?.isVisible() = (this?.properties?.get("visible") as Boolean?) ?: false

fun TiledMapTile?.setVisible(value: Boolean?) {
    this?.properties?.put("visible", value)
}
