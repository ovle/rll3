package com.ovle.rll3

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.view.screenHeight
import com.ovle.rll3.view.screenWidth
import com.ovle.rll3.view.skinPath
import ktx.app.KtxGame
import ktx.async.KtxAsync
import ktx.inject.Context
import ktx.scene2d.Scene2DSkin


class RLL3Game : KtxGame<BaseScreen>() {

    private lateinit var context: Context
    private val disposables = mutableListOf<Disposable>()


    override fun create() {
        KtxAsync.initiate()

        context = disposable(Context())
        val screenManager = disposable(
            ScreenManager(context) {
                bs: BaseScreen, payload: Any? ->
                bs.payload = payload
                setScreen(bs.javaClass)
            }
        )
        val assetManager = disposable(AssetManager())

        val skin = Skin(Gdx.files.internal(skinPath))
        val font = skin.getFont("commodore-64")
        font.data.scale(-0.1f)
        Scene2DSkin.defaultSkin = disposable(skin)

        context.register {
            bindSingleton<Batch>(disposable(SpriteBatch()))
            bindSingleton(assetManager)
            bindSingleton(AssetsManager(assetManager))
            bindSingleton(camera())
            bindSingleton(screenManager)
        }

        screenManager.screens().forEach { addScreen(it.javaClass, it) }
        setScreen(screenManager.initScreen())

        super.create()
    }

    override fun dispose() {
        disposables.forEach { it.dispose() }

        super.dispose()
    }


    private fun camera() =
        OrthographicCamera().apply {
            setToOrtho(false, screenWidth, screenHeight);
            update()
        }

    private inline fun <reified T : Disposable> disposable(d: T) = d.apply { disposables.add(d) }
}