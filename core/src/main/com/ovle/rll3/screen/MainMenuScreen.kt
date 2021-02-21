package com.ovle.rll3.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Align
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rlUtil.gdx.screen.BaseScreen
import com.ovle.rll3.event.SwitchScreenCommand
import com.ovle.rll3.screen.game.InitPlaygroundInfo
import com.ovle.rll3.screen.game.PlaygroundScreen
import com.ovle.util.screen.ScreenConfig
import ktx.actors.onClick
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup


class MainMenuScreen(
    batch: Batch,
    camera: OrthographicCamera,
    screenConfig: ScreenConfig
) : BaseScreen(
    batch,
    camera,
    screenConfig
) {

    override fun rootActor() =
        scene2d.verticalGroup {
            label(text = "The game") {}

            textButton(text = "Play") {
                onClick { send(SwitchScreenCommand(WorldScreen::class.java)) }
            }
            textButton(text = "Options") {
//                onClick { send(SwitchScreenCommand(OptionsScreen::class.java)) }
            }
            textButton(text = "Playground") {
                onClick { send(SwitchScreenCommand(PlaygroundScreen::class.java, InitPlaygroundInfo())) }
            }
            textButton(text = "Exit") {
                onClick { Gdx.app.exit() }
            }

            setFillParent(true)
            align(Align.center)
            pack()
        }
}