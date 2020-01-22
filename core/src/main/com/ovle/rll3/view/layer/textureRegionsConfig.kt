package com.ovle.rll3.view.layer

import com.badlogic.gdx.graphics.g2d.TextureRegion

fun portalTR(regions: Array<Array<TextureRegion>>): Array<TextureRegion> {
    return arrayOf(
        regions[10][8],
        regions[10][9],
        regions[10][10],
        regions[10][11],
        regions[11][8],
        regions[11][9],
        regions[11][10],
        regions[11][11]
    )
}

fun trapsTR(regions: Array<Array<TextureRegion>>): Array<TextureRegion> {
    return arrayOf(
        regions[7][8],
        regions[7][9],
        regions[7][10],
        regions[7][11]
    )
}

fun lightTR(regions: Array<Array<TextureRegion>>): Array<TextureRegion> {
    return arrayOf(
        regions[8][8],
        regions[8][9],
        regions[8][10],
        regions[8][11]
    )
}
fun lightWallTR(regions: Array<Array<TextureRegion>>): Array<TextureRegion> {
    return arrayOf(
        regions[9][8],
        regions[9][9],
        regions[9][10],
        regions[9][11]
    )
}