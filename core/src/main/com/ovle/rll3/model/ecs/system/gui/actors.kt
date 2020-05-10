package com.ovle.rll3.model.ecs.system.gui

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.view.layer.image
import com.ovle.rll3.view.layer.label as cLabel
import ktx.actors.onClick
import ktx.ashley.get
import ktx.scene2d.*

fun entityActionsActor(actions: Collection<String>, entity: Entity, onActionClick: (String) -> Unit): KWindow {
    val templateName = entity[Mappers.template]?.template?.name ?: "no name"
    return window(
            title = templateName,
            style = "default"
    ) {
        center()

        verticalGroup {
            actions.mapIndexed { i, action ->
                textButton(text = action) {
                    onClick {
                        onActionClick(action)
                    }
                }
            }
        }.cell(pad = 10.0f)

        pack()
    }
}

//todo refactor
fun entityInfoActor(panelInfo: EntityPanelInfo, guiTexture: Texture): Actor {
    val scale = 4.0f
    val portrait = TextureRegion(guiTexture, 2 * 24, 0, 24, 24)
    val bg = TextureRegion(guiTexture, 120, 0, 72, 40)
    val pi = image(portrait).also { panelInfo.portraitWidget = it }

    val leftPart = table {
        add(pi).size(24 * scale, 24 * scale)
                .padRight(2.0f * scale).padBottom(1.0f * scale)
        row()
        add(cLabel().also { panelInfo.nameWidget = it }).padTop(2 * scale)
    }

    val rightPart = table {
        defaults().padBottom(5 * scale).padLeft(10 * scale)

        add(cLabel().also {
            panelInfo.healthInfoWidget = it;
//            it.style.fontColor = Palette.whiteColor
        })
                .padTop(6 * scale)
        row()
        label("00/00")
        row()
        add(cLabel().also { panelInfo.staminaInfoWidget = it })
        row()
        label("00/00")
    }

    val percentWidth50 = Value.percentWidth(50.0f)
    val result = table {
        height = bg.regionHeight * scale
        width = bg.regionWidth * scale
        background = TextureRegionDrawable(bg)

        add(leftPart).width(percentWidth50).expand()
        add(rightPart).width(percentWidth50).expand()
    }

    return result
}

fun worldInfoActor(worldPanelInfo: WorldPanelInfo, guiTexture: Texture): Actor {
    val scale = 4.0f
    val bg = TextureRegion(guiTexture, 192, 0, 34, 40)
    val fullWidth = Value.percentWidth(100.0f)

    val worldInfo = table {
        add(cLabel().also { worldPanelInfo.levelNameWidget = it })
    }
    val timeInfo = table {
        add(cLabel().also { worldPanelInfo.timeWidget = it })
    }

    val result = table {
        height = bg.regionHeight * scale
        width = bg.regionWidth * scale
        background = TextureRegionDrawable(bg)

        add(worldInfo).width(fullWidth).expand()
        row()
        add(timeInfo).width(fullWidth).expand()
    }

    return result
}