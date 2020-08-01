package com.ovle.rll3.screen

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.utils.Align
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.ScreenManager.ScreenType.MainMenuScreenType
import com.ovle.rll3.model.template.entity.entityTemplates
import com.ovle.rll3.persistance.Preferences
import com.ovle.rll3.view.layer.selectBox
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.scene2d.*
import com.ovle.rll3.view.layer.label as cLabel


class ManageScreen(
    screenManager: ScreenManager, batch: Batch, camera: OrthographicCamera
) : BaseScreen(screenManager, batch, camera) {

    private fun playableTemplates() = entityTemplates().templates.filter { it.playable ?: false }

    override fun rootActor(): KTableWidget {
        val mainHeight = Value.percentHeight(80.0f)

        return table {
            debugAll()
            row()
            add(cLabel(text = "Manage")).colspan(4).pad(20.0f)

            row().height(mainHeight).expandY().fillY()
            add(leftMainGroup()).colspan(2)
            add(rightMainGroup()).colspan(2)

            row()
            add(buttonsGroup()).colspan(4).pad(20.0f)

            setFillParent(true)
            align(Align.center)
            pack()
        }
    }

    private fun leftMainGroup(): Actor {
        val templateNames = playableTemplates().map { it.name }
            return table {
                row()
                add(cLabel(text = "Hero")).pad(10.0f)
                row()
                add(
                    selectBox(templateNames, Preferences.playerTemplateName()) {
                        onChange {
                            Preferences.setPlayerTemplateName(selected)
                        }
                    }
                ).fillX().expandX()

                row()
                add(cLabel(text = "World")).pad(10.0f)
                //todo
            }
    }

    private fun rightMainGroup(): Actor {
        return verticalGroup {
            label(text = "Test") {}
        }
    }

    private fun buttonsGroup(): Actor {
        return horizontalGroup {
            textButton(text = "Save") {
                onClick {
                    Preferences.flush()
                }
            }
            textButton(text = "Back") {
                onClick {
                    screenManager.goToScreen(MainMenuScreenType)
                }
            }
        }
    }
}