package com.ovle.rll3.view

import com.badlogic.gdx.graphics.Color

object Palette {
    val blackColor: Color = Color.valueOf("272744ff")
    val darkColor: Color = Color.valueOf("494d7eff")
    val middleColor: Color = Color.valueOf("8b6d9cff")
    val lightRedColor: Color = Color.valueOf("c69fa5ff")
//    val lightYellowColor: Color = Color.valueOf("f2d3abff")
    val whiteColor: Color = Color.valueOf("fbf5efff")

    private val orderedColors = arrayOf(
        blackColor, darkColor, middleColor, lightRedColor, /*lightYellowColor,*/ whiteColor
    )

    fun next(color: Color): Color {
        return when (val index = orderedColors.indexOf(color)) {
            -1 -> color                         //out of palette
            orderedColors.size - 1 -> color     //already last
            else -> orderedColors[index + 1]
        }
    }

    fun prev(color: Color): Color {
        return when (val index = orderedColors.indexOf(color)) {
            -1 -> color                         //out of palette
            0 -> color                          //already first
            else -> orderedColors[index - 1]
        }
    }
}