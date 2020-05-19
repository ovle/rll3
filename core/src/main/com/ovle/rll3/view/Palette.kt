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

//------------------------------------------------
    private val mappedPalette = arrayOf(
        black,
        darkGray,
        grayBrown,
        lightBrown,
        white
    )

    private val paletteNight = arrayOf(
        black,
        darkBlue,
        blue,
        aquamarine,
        white
    )

    private val paletteSunset = arrayOf(
        black,
        darkPurple,
        brown,
        orange,
        white
    )

//------------------------------------------------
    private val paletteRed4Color = arrayOf(
        darkGray,
        red,
        red,
        orange,
        white
    )

    private val paletteGreen4Color = arrayOf(
        darkPurple,
        darkGreen,
        darkGreen,
        lightGreen,
        yellow
    )

//------------------------------------------------

    private val palette3Color = arrayOf(
        black,
        grayBrown,
        white,
        grayBrown,
        white
    )

    private val paletteFire3Color = arrayOf(
        darkPurple,
        red,
        red,
        yellow,
        yellow
    )

    private val paletteWater3Color = arrayOf(
        darkBlue,
        blue,
        blue,
        aquamarine,
        aquamarine
    )

    private val paletteLight3Color = arrayOf(
        darkGray,
        gray,
        white,
        gray,
        white
    )

    private val paletteDark3Color = arrayOf(
        black,
        darkPurple,
        red,
        darkPurple,
        red
    )

    private val paletteNature3Color = arrayOf(
        darkPurple,
        darkGreen,
        lightBrown,
        darkGreen,
        lightBrown
    )

//------------------------------------------------

    private val paletteReverse = arrayOf(
        white,
        orange,
        brown,
        brown,
        black
    )

    private val palette2color = arrayOf(
        black,
        white,
        white,
        white,
        white
    )

    private val palette = palette3Color

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