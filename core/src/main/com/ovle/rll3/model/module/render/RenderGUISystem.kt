package com.ovle.rll3.model.module.render

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.model.module.core.component.ComponentMappers.game
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.component.ComponentMappers.template
import com.ovle.rll3.model.module.core.entity.*
import com.ovle.rll3.view.fontName
import com.ovle.rll3.view.palette.Palette
import ktx.ashley.get
import ktx.math.vec2
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
    private val dy = 20.0f


    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        stageBatch.begin()
        drawSystemInfo()
        stageBatch.end()
    }

    private fun drawSystemInfo() {
        val interaction = playerInteraction()!!
        val selectedPoint = interaction[position]!!.gridPosition

        val interactionInfo = playerInteractionInfo()!!
        val hoveredEntity = interactionInfo.hoveredEntity
//        val selectedEntity = interactionInfo.selectedEntity
//        val focusedEntity = interactionInfo.focusedEntity

        val locationInfo = locationInfo()
        val locationPoint = locationInfo.locationPoint

        val game = gameInfo()!!
        val time = game.time
        val worldAreaName = game.world.area(locationPoint).name

        val point = vec2(0.0f, Gdx.graphics.height - dy)
        val info = arrayOf(
            "$worldAreaName $locationPoint",
            "turn ${time.turn}",
            "$selectedPoint " + (hoveredEntity?.let { "(${it.name()})" } ?: "")
        ).filterNotNull()

        info.forEachIndexed {
            i, _ ->
            font.draw(stageBatch, info[i], point.x, point.y - dy * i)
        }
    }
}