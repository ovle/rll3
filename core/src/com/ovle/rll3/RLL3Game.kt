package com.ovle.rll3

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.view.screenHeight
import com.ovle.rll3.view.screenWidth
import com.ovle.rll3.view.skinPath
import ktx.app.KtxGame
import ktx.inject.Context
import ktx.scene2d.Scene2DSkin


class RLL3Game : KtxGame<BaseScreen>() {
    private lateinit var context: Context
    private lateinit var skin: Skin
    private lateinit var spriteBatch: SpriteBatch
    private lateinit var screenManager: ScreenManager

    override fun create() {
        context = Context()
        skin = Skin(Gdx.files.internal(skinPath))
        Scene2DSkin.defaultSkin = skin

        spriteBatch = SpriteBatch()
        screenManager = ScreenManager(context) { bs: BaseScreen -> setScreen(bs.javaClass) }

        context.register {
            bindSingleton<Batch>(spriteBatch)
            bindSingleton(AssetManager())
            bindSingleton(
                OrthographicCamera().apply {
                    setToOrtho(false, screenWidth, screenHeight); update()
                }
            )
            bindSingleton(screenManager)

            screenManager.screens().forEach { addScreen(it.javaClass, it) }
        }

        setScreen(screenManager.initScreen())

        super.create()
    }

    override fun dispose() {
        context.dispose()
        skin.dispose()
        spriteBatch.dispose()

        super.dispose()
    }
}