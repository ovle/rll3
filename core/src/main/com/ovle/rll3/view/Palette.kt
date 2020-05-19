package com.ovle.rll3.view

import com.badlogic.gdx.graphics.Color


object Palette {
    
    private val black = Color.valueOf("140C1Cff")
    private val darkPurple = Color.valueOf("442434ff")
    private val darkBlue = Color.valueOf("30346Dff")
    private val darkGray = Color.valueOf("4E4A4Eff")
    private val brown = Color.valueOf("854C30ff")
    private val darkGreen = Color.valueOf("346524ff")
    private val red = Color.valueOf("D04648ff")
    private val grayBrown = Color.valueOf("757161ff")
    private val blue = Color.valueOf("597DCEff")
    private val orange = Color.valueOf("D27D2Cff")
    private val gray = Color.valueOf("8595A1ff")
    private val lightGreen = Color.valueOf("6DAA2Cff")
    private val lightBrown = Color.valueOf("D2AA99ff")
    private val aquamarine = Color.valueOf("6DC2CAff")
    private val yellow = Color.valueOf("DAD45Eff")
    private val white = Color.valueOf("DEEED6ff")


    private val mappedPalette = arrayOf(
        black,
        darkGray,
        grayBrown,
        lightBrown,
        white
    )

    private val palette0 = arrayOf(
        black,
        darkBlue,
        blue,
        aquamarine,
        white
    )

    private val palette1 = arrayOf(
        black,
        darkGray,
        red,
        orange,
        yellow
    )
    private val palette2 = arrayOf(
        black,
        darkPurple,
        darkGreen,
        lightGreen,
        white
    )

    private val palette3 = arrayOf(
        black,
        darkPurple,
        brown,
        orange,
        white
    )

    private val palette4 = arrayOf(
        white,
        orange,
        brown,
        darkPurple,
        black
    )

    private val palette5 = arrayOf(
        black,
        grayBrown,
        white,
        grayBrown,
        white
    )

    private val palette6 = arrayOf(
        black,
        white,
        white,
        white,
        white
    )


    private val palette = palette5

    val darkestColor: Color = palette.first()
    val lightestColor: Color = palette.last()

    fun next(color: Color): Color {
        //todo test
        if (color == darkestColor) return color

        return when (val index = palette.indexOf(color)) {
            -1 -> color                         //out of palette
            palette.size - 1 -> color     //already last
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