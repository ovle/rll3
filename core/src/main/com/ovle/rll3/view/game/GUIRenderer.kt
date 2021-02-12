package com.ovle.rll3.view.game

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Queue
import com.ovle.rlUtil.gdx.math.Direction
import com.ovle.rlUtil.gdx.math.point
import com.ovle.rlUtil.gdx.math.rangeX
import com.ovle.rlUtil.gdx.math.rangeY
import com.ovle.rlUtil.gdx.view.PaletteManager
import com.ovle.rlUtil.gdx.view.Sprite
import com.ovle.rlUtil.gdx.view.sprite
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.core.entity.*
import com.ovle.rll3.model.module.game.AreaInfo
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.interaction.PlayerInteractionComponent
import com.ovle.rll3.model.module.render.draw
import com.ovle.rll3.model.module.task.TaskInfo
import com.ovle.rll3.model.util.info
import com.ovle.rll3.view.fontName
import com.ovle.rll3.view.spriteSize
import ktx.ashley.get
import ktx.math.vec2
import ktx.scene2d.Scene2DSkin

class GUIRenderer(
    private val batch: Batch,
    private val stageBatch: Batch,
    assetsManager: AssetsManager,
    paletteManager: PaletteManager,
    private val engine: Engine
): BaseRenderer {

    private val font = Scene2DSkin.defaultSkin.getFont(fontName)
        .apply {
//            data.scale(-0.6f)
            color = paletteManager.palette.last().cpy()
        }
    private val dy = 20.0f

    private val guiRegions = TextureRegion.split(assetsManager.guiTexture, spriteSize.toInt(), spriteSize.toInt())

    private val selectionEntitySprite = sprite(guiRegions, 1, 0)
    private val hoverSprite = sprite(guiRegions, 2, 0)
    private val cursorSprite = sprite(guiRegions, 3, 0)

    private val recLeftSprite = sprite(guiRegions, 4, 0)
    private val recRightSprite = sprite(guiRegions, 7, 0)
    private val recTopSprite = sprite(guiRegions, 5, 0)
    private val recBottomSprite = sprite(guiRegions, 6, 0)

    private val areaTileSprite = sprite(guiRegions, 7, 1)


    override fun render(deltaTime: Float) {
        val interaction = engine.playerInteraction()!!
        val game = engine.game()!!

        stageBatch.begin()
        renderGUI(interaction, game)
        stageBatch.end()

        batch.begin()
        renderInteractionInfo(interaction, game)
        batch.end()
    }

    private fun renderGUI(interaction: Entity, game: Entity) {
        val selectedPoint = interaction.position()

        val interactionInfo = interaction[ComponentMappers.playerInteraction]!!
        val hoveredEntity = interactionInfo.hoveredEntity
        val selectedEntity = interactionInfo.selectedEntity
//        val focusedEntity = interactionInfo.focusedEntity

        val gameInfo = game[ComponentMappers.game]!!
        val locationInfo = gameInfo.location
        val locationPoint = locationInfo.locationPoint
        val time = gameInfo.time
        val worldAreaName = gameInfo.world.area(locationPoint).name
        val tasksInfo = game[ComponentMappers.tasks]!!

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

    private fun renderInteractionInfo(interaction: Entity, game: Entity) {
        drawCursor(interaction)

        val interactionComponent = interaction[ComponentMappers.playerInteraction]!!
        val locationInfo = game[ComponentMappers.game]!!.location
        drawSelection(interactionComponent)
//        drawControl(interactionComponent)
        drawHover(interactionComponent)
        drawAreas(interactionComponent, locationInfo)
    }

    private fun drawCursor(entity: Entity) {
        draw(entity, cursorSprite)
    }

    private fun drawSelection(ic: PlayerInteractionComponent) {
        val selectedEntity = ic.selectedEntity
        selectedEntity?.let { draw(selectedEntity, selectionEntitySprite) }

        val selectionRectangle = ic.selectionRectangle ?: return
        drawRectangle(selectionRectangle)
    }

    //todo not interaction info?
    private fun drawAreas(ic: PlayerInteractionComponent, locationInfo: LocationInfo) {
        val areas = locationInfo.areas
        areas.forEach { drawArea(it) }

        drawSelectedArea(ic)
    }

    private fun drawSelectedArea(ic: PlayerInteractionComponent) {
        val selectedArea = ic.selectedArea?.area ?: return
        selectedArea.borders(Direction.H, -1).forEach {
            draw(it, recLeftSprite)
        }
        selectedArea.borders(Direction.H, 1).forEach {
            draw(it, recRightSprite)
        }
        selectedArea.borders(Direction.V, 1).forEach {
            draw(it, recTopSprite)
        }
        selectedArea.borders(Direction.V, -1).forEach {
            draw(it, recBottomSprite)
        }
    }

    private fun drawArea(areaInfo: AreaInfo) {
        areaInfo.area.points.forEach {
            draw(it, areaTileSprite)
        }
    }

    private fun drawRectangle(selectedArea: Rectangle) {
        val rangeX = selectedArea.rangeX()
        val rangeY = selectedArea.rangeY()
        rangeX.forEach {
            draw(point(it, rangeY.first), recBottomSprite)
            draw(point(it, rangeY.last), recTopSprite)
        }
        rangeY.forEach {
            draw(point(rangeX.first, it), recLeftSprite)
            draw(point(rangeX.last, it), recRightSprite)
        }
    }

    //todo highlight sprite instead
    private fun drawHover(ic: PlayerInteractionComponent) {
        val hoveredEntity = ic.hoveredEntity ?: return
//        hoveredEntity[render]!!.switchSprite()
//        draw(hoveredEntity)
        draw(hoveredEntity, hoverSprite)
    }

    private fun draw(entity: Entity, sprite: Sprite) {
        val position = entity.position()
        draw(position, sprite)
    }

    private fun draw(position: GridPoint2, sprite: Sprite) {
        val region = sprite.textureRegion()

        batch.draw(position, region)
    }
}