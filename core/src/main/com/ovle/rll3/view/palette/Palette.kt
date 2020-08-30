package com.ovle.rll3.view.palette

import com.badlogic.gdx.graphics.Color


object Palette {

    /**
     * resources palette
     */
    private val mappedPalette = paletteDay

    /**
     * in-game displayed palette
     */
    val palette = paletteMain

    /**
     * in-game background color (usually the most dark color of the palette)
     */
    val bgColor = palette.first().cpy()


    private val darkestColor: Color = palette.first()
    private val lightestColor: Color = palette.last()

    fun next(color: Color): Color {
//        if (color == darkestColor) return color

        return when (val index = palette.indexOf(color)) {
            -1 -> color                         //out of palette
            palette.size - 1 -> color           //already last
            else -> palette[index + 1]
        }
    }

    fun prev(color: Color): Color {
        return when (val index = palette.indexOf(color)) {
            -1 -> color                         //out of palette
            0 -> color                          //already first
            else -> palette[index - 1]
        }
    }

    fun map(color: Color): Color {
        val index = mappedPalette.indexOf(color)
        return if (index == -1) color else palette[index]
    }
}