package com.ovle.rll3.model.module.render

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.entity.locationInfo
import com.ovle.rll3.model.module.core.entity.playerInteraction
import com.ovle.rll3.view.fontName
import com.ovle.rll3.view.palette.Palette
import ktx.ashley.get
import ktx.scene2d.Scene2DSkin


class RenderGUISystem(
    private val batch: Batch,
    assetsManager: AssetsManager,
    private val stageBatch: Batch
) : EntitySystem() {

//    private val guiRegions = split(assetsManager.guiTexture.texture, spriteWidth.toInt(), spriteHeight.toInt())

    private val font = Scene2DSkin.defaultSkin.getFont(fontName)
        .apply {
//            data.scale(-0.6f)
            color = Palette.palette.last().cpy()
        }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        drawSystemInfo()
    }

    private fun drawSystemInfo() {
        val interaction = playerInteraction()!!
        val selectedPoint = interaction[position]!!.gridPosition
        val locationPoint = locationInfo().locationPoint
        val worldAreaName = "test lands"

        val point = ktx.math.vec2(0.0f, Gdx.graphics.height - 20.0f)
        val info = "$worldAreaName, location $locationPoint ($selectedPoint)"

        stageBatch.begin()
        font.draw(stageBatch, info, point.x, point.y)
        stageBatch.end()
    }
}