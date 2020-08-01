package com.ovle.rll3.model.ecs.system.render

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion.split
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.special.PlayerInteractionComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.playerInteraction
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.entity.allEntities
import com.ovle.rll3.model.ecs.entity.playerInteraction
import com.ovle.rll3.view.sprite.Sprite
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import ktx.ashley.get


class RenderInteractionInfoSystem(
    private val batch: Batch,
    assetsManager: AssetsManager
) : EntitySystem() {

    private val guiRegions = split(assetsManager.guiTexture.texture, spriteWidth.toInt(), spriteHeight.toInt())

    private val controlSprite = sprite(guiRegions, 1, 0)
    private val selectionSprite = sprite(guiRegions, 1, 0)
    private val hoverSprite = sprite(guiRegions, 2, 0)

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        batch.begin()
        drawInteractionInfo(playerInteraction(this.allEntities().toList()))
        batch.end()
    }

    private fun drawInteractionInfo(interaction: Entity?) {
        if (interaction == null) return

        val interactionComponent = interaction[playerInteraction]!!
        drawSelection(interactionComponent)
        drawControl(interactionComponent)
        drawHover(interactionComponent)
    }

    private fun drawSelection(interactionComponent: PlayerInteractionComponent) {
        val selectedEntity = interactionComponent.selectedEntity ?: return
        draw(selectedEntity, selectionSprite)
    }

    private fun drawControl(interactionComponent: PlayerInteractionComponent) {
        val controlledEntity = interactionComponent.controlledEntity ?: return
        draw(controlledEntity, controlSprite)
    }

    private fun drawHover(interactionComponent: PlayerInteractionComponent) {
        val hoveredEntity = interactionComponent.hoveredEntity ?: return
        draw(hoveredEntity, hoverSprite)
    }

    private fun draw(entity: Entity, sprite: Sprite) {
        val position = entity[position]!!.gridPosition
        val region = sprite.textureRegion()

        batch.draw(floatPoint(position), region)
    }
}