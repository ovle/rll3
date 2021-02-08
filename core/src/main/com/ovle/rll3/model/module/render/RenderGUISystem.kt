package com.ovle.rll3.model.module.render

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Queue
import com.ovle.rlUtil.gdx.view.PaletteManager
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.model.module.core.entity.*
import com.ovle.rll3.model.module.task.TaskInfo
import com.ovle.rll3.model.util.info
import com.ovle.rll3.view.fontName
import ktx.math.vec2
import ktx.scene2d.Scene2DSkin


class RenderGUISystem(
    private val batch: Batch,
    private val stageBatch: Batch,
    assetsManager: AssetsManager,
    paletteManager: PaletteManager
) : EntitySystem() {

//    private val guiRegions = split(assetsManager.guiTexture.texture, spriteWidth.toInt(), spriteHeight.toInt())

    private val font = Scene2DSkin.defaultSkin.getFont(fontName)
        .apply {
//            data.scale(-0.6f)
            color = paletteManager.palette.last().cpy()
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
        val selectedPoint = interaction.position()

        val interactionInfo = playerInteractionInfo()!!
        val tasksInfo = tasksInfo()!!
        val hoveredEntity = interactionInfo.hoveredEntity
        val selectedEntity = interactionInfo.selectedEntity
//        val focusedEntity = interactionInfo.focusedEntity

        val locationInfo = locationInfo()
        val locationPoint = locationInfo.locationPoint

        val game = gameInfo()!!
        val time = game.time
        val worldAreaName = game.world.area(locationPoint).name

        val point = vec2(0.0f, Gdx.graphics.height - dy)
        val info = arrayOf(
            "$worldAreaName $locationPoint",
            "turn ${time.turn.toLong()} (x${time.turnsInSecond})" + if (time.paused) " (pause)" else "",
            "$selectedPoint " + (hoveredEntity?.let { "(${it.name()})" } ?: ""),
            selectedEntity?.let { it.info(recursive = true) },
            tasksInfo(tasksInfo.tasks)?.let {
                "tasks:\n$it \n(total: ${tasksInfo.tasks.size})"
            }
        ).filterNotNull().joinToString("\n")

        font.draw(stageBatch, info, point.x, point.y)
    }

    private fun tasksInfo(tasks: Queue<TaskInfo>): String? {
        if (tasks.isEmpty) return null

        return tasks.joinToString(separator = "\n") {
            val name = it.template.btTemplate.name
            val performer = if (it.performer == null) "" else "(${it.performer!!.info()})"
            val target = it.target.target.info()
            "   $name $target: ${it.status} $performer"
        }
    }
}