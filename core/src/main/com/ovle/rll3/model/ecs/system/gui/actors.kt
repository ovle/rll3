package com.ovle.rll3.model.ecs.system.gui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.ovle.rll3.view.layer.image
import com.ovle.rll3.view.layer.textArea
import com.ovle.rll3.view.layer.textButton
import com.ovle.rll3.view.layer.label as cLabel
import ktx.actors.onClick
import ktx.scene2d.*

const val guiScale = 4.0f

fun entityActionsActor(actions: Collection<String>, onActionClick: (String) -> Unit): KWindow {
    return window(
            title = "",
            style = "dialog"
    ) {
        width = 72 * guiScale

        verticalGroup {
            actions.mapIndexed { i, action ->
                textButton(text = action) {
                    onClick {
                        onActionClick(action)
                    }
                }
            }
        }.cell(pad = 5f)
    }
}

//todo refactor
fun entityInfoActor(panelInfo: EntityPanelInfo, guiTexture: Texture): Actor {
    val portrait = TextureRegion(guiTexture, 2 * 24, 0, 24, 24)
    val bg = TextureRegion(guiTexture, 120, 0, 72, 41)
    val pi = image(portrait).also { panelInfo.portraitWidget = it }

    val leftPart = table {
        add(pi).size(24 * guiScale, 24 * guiScale)
                .padRight(2.0f * guiScale).padBottom(1.0f * guiScale)
        row()
        add(cLabel().also { panelInfo.nameWidget = it }).padTop(2 * guiScale)
    }

    val rightPart = table {
        defaults().padBottom(5 * guiScale).padLeft(10 * guiScale)

        add(cLabel().also {
            panelInfo.healthInfoWidget = it;
//            it.style.fontColor = Palette.whiteColor
        }).padTop(6 * guiScale)
        row()
        label("-/-")
        row()
        add(cLabel().also { panelInfo.staminaInfoWidget = it })
        row()
        label("-/-")
    }

    val percentWidth50 = Value.percentWidth(50.0f)
    val result = table {
        height = bg.regionHeight * guiScale
        width = bg.regionWidth * guiScale
        background = TextureRegionDrawable(bg)

        add(leftPart).width(percentWidth50).expand()
        add(rightPart).width(percentWidth50).expand()
    }

    return result
}

fun worldInfoActor(worldPanelInfo: WorldPanelInfo, guiTexture: Texture): Actor {
    val bg = TextureRegion(guiTexture, 192, 0, 40, 41)
    val fullWidth = Value.percentWidth(100.0f)

    val worldInfo = table {
        add(cLabel().also { worldPanelInfo.levelNameWidget = it })
    }
    val timeInfo = table {
        add(cLabel().also { worldPanelInfo.timeWidget = it })
    }

    val result = table {
        height = bg.regionHeight * guiScale
        width = bg.regionWidth * guiScale
        background = TextureRegionDrawable(bg)

        add(worldInfo).width(fullWidth).expand()
        row()
        add(timeInfo).width(fullWidth).expand()
    }

    return result
}

fun logActor(logPanelInfo: LogPanelInfo, guiTexture: Texture): Actor {
    val bg = TextureRegion(guiTexture, 120, 41, 112, 41)

    val textInfo = textArea("") {
        width = guiScale * 40
        isDisabled = true
        logPanelInfo.textWidget = this
    }
    val toggleButton = textButton("log") {
        width = guiScale * 40
        onClick {
            logPanelInfo.opened = !logPanelInfo.opened
//            todo
        }
    }

    val result = table {
        height = bg.regionHeight * guiScale
        width = bg.regionWidth * guiScale
        background = TextureRegionDrawable(bg)

        add(textInfo)
            .height(this.height * 0.9f).width(this.width * 0.95f)
            .expand()
        row()
//        add(toggleButton)
//            .height(this.height * 0.2f).width(this.width)
//            .expand()
    }

    return result
}