package com.ovle.rll3.view

import com.badlogic.gdx.graphics.Color

object Palette {
    private val mappedPalette = arrayOf(
        Color.valueOf("272744ff"),  //r 0.286 g 0.302 b 0.494
        Color.valueOf("494D7Eff"),
        Color.valueOf("8B6D9Cff"),  //r 0.545 g 0.427 b 0.611
        Color.valueOf("C69FA5ff"),
//        Color.valueOf("F2D3ABff"),
        Color.valueOf("FBF5EFff")   //r 0.984 g 0.96 b 0.937
    )

    private val paltte0 = arrayOf(
        Color.valueOf("140C1Cff"),
        Color.valueOf("30346Dff"),
        Color.valueOf("597DCEff"),
        Color.valueOf("DEEED6ff")
    )

    private val palette1 = arrayOf(
        Color.valueOf("140C1Cff"),
        Color.valueOf("4E4A4Eff"),
        Color.valueOf("D04648ff"),
        Color.valueOf("DAD45Eff")
    )

    private val palette2 = arrayOf(
        Color.valueOf("140C1Cff"),
        Color.valueOf("442434ff"),
        Color.valueOf("346524ff"),
        Color.valueOf("6DAA2Cff")
    )

    private val palette3 = arrayOf(
        Color.valueOf("140C1Cff"),
        Color.valueOf("854C30ff"),
        Color.valueOf("D27D2Cff"),
        Color.valueOf("DEEED6ff")
    )

    private val palette4 = arrayOf(
        Color.valueOf("140C1Cff"),
        Color.valueOf("4E4A4Eff"),
        Color.valueOf("757161ff"),
        Color.valueOf("DEEED6ff")
    )

    private val palette = mappedPalette

    val blackColor: Color = palette.first()
    val whiteColor: Color = palette.last()

    fun next(color: Color): Color {
        //todo test
        if (color == blackColor) return color

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