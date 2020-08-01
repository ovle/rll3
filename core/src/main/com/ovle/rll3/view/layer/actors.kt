package com.ovle.rll3.view.layer

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Scaling
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actor
import ktx.scene2d.defaultStyle

inline fun image(
    tr: TextureRegion,
    scaling: Scaling = Scaling.fill,
    init: Image.() -> Unit = {}
) = actor(Image(TextureRegionDrawable(tr), scaling), init)

inline fun textButton(
    text: String,
    style: String = defaultStyle,
    init: TextButton.() -> Unit = {}
) = actor(TextButton(text, Scene2DSkin.defaultSkin, style), init)

inline fun label(
    text: String = "",
    style: String = defaultStyle,
    init: Label.() -> Unit = {}
) = actor(Label(text, Scene2DSkin.defaultSkin, style), init)

inline fun textField(
    text: String,
    style: String = defaultStyle,
    init: TextField.() -> Unit = {}
) = actor(TextField(text, Scene2DSkin.defaultSkin, style), init)

inline fun textArea(
    text: String,
    style: String = "nobg",
    init: TextArea.() -> Unit = {}
) = actor(TextArea(text, Scene2DSkin.defaultSkin, style), init)

inline fun selectBox(
    initItems: Collection<String>,
    selected: String,
    init: SelectBox<String>.() -> Unit = {}
) = actor(SelectBox<String>(Scene2DSkin.defaultSkin), init).apply {
    this.items = Array<String>(initItems.size).apply {
        addAll(*initItems.toTypedArray())
    }
}