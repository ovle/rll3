package com.ovle.rll3.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.ovle.rll3.view.fontName
import com.ovle.rll3.view.skinPath

fun skin() = Skin(Gdx.files.internal(skinPath)).apply {
    getFont(fontName).apply { data.scale(-0.1f) }
}