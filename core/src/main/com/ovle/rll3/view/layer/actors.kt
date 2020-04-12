package com.ovle.rll3.view.layer

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor

inline fun <S> KWidget<S>.imageFromTexture(
    tr: TextureRegion,
    init: (@Scene2dDsl Image).(S) -> Unit = {}) = actor(Image(tr), init)