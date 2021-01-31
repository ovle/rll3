package com.ovle.rll3.model.module.render

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion.split
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Rectangle
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.model.module.core.component.ComponentMappers.playerInteraction
import com.ovle.rll3.model.module.core.entity.locationInfo
import com.ovle.rll3.model.module.core.entity.playerInteraction
import com.ovle.rll3.model.module.core.entity.position
import com.ovle.rll3.model.module.game.AreaInfo
import com.ovle.rll3.model.module.interaction.PlayerInteractionComponent
import com.ovle.rlUtil.gdx.math.Direction
import com.ovle.rlUtil.gdx.math.point
import com.ovle.rlUtil.gdx.math.rangeX
import com.ovle.rlUtil.gdx.math.rangeY
import com.ovle.rlUtil.gdx.math.vec2
import com.ovle.rll3.view.sprite.Sprite
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import ktx.ashley.get


class RenderInteractionInfoSystem(
    private val batch: Batch,
    assetsManager: AssetsManager
) : EntitySystem() {

    private val guiRegions = split(assetsManager.guiTexture.texture, spriteWidth.toInt(), spriteHeight.toInt())

    private val selectionEntitySprite = sprite(guiRegions, 1, 0)
    private val hoverSprite = sprite(guiRegions, 2, 0)
    private val cursorSprite = sprite(guiRegions, 3, 0)

    private val recLeftSprite = sprite(guiRegions, 4, 0)
    private val recRightSprite = sprite(guiRegions, 7, 0)
    private val recTopSprite = sprite(guiRegions, 5, 0)
    private val recBottomSprite = sprite(guiRegions, 6, 0)

    private val areaTileSprite = sprite(guiRegions, 7, 1)


    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        batch.begin()
        drawInteractionInfo(playerInteraction())
        batch.end()
    }

    private fun drawInteractionInfo(interaction: Entity?) {
        if (interaction == null) return
        drawCursor(interaction)

        val interactionComponent = interaction[playerInteraction]!!
        drawSelection(interactionComponent)
//        drawControl(interactionComponent)
        drawHover(interactionComponent)
        drawAreas(interactionComponent)
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
    private fun drawAreas(ic: PlayerInteractionComponent) {
        val location = locationInfo()
        val areas = location.areas
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

        batch.draw(vec2(position), region)
    }
}