package com.ovle.rll3.model.ecs.system.render

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion.split
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.model.ecs.component.special.PlayerInteractionComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.playerInteraction
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.entity.allEntities
import com.ovle.rll3.model.ecs.entity.playerInteraction
import com.ovle.rll3.vec2
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
    private val selectionTileSprite = sprite(guiRegions, 4, 0)

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        batch.begin()
        drawInteractionInfo(playerInteraction(this.allEntities().toList()))
        batch.end()
    }

    private fun drawInteractionInfo(interaction: Entity?) {
        if (interaction == null) return
        drawCursor(interaction)

        val interactionComponent = interaction[playerInteraction]!!
        drawSelection(interactionComponent)
//        drawControl(interactionComponent)
        drawHover(interactionComponent)
    }

    private fun drawCursor(entity: Entity) {
        draw(entity, cursorSprite)
    }

    private fun drawSelection(interactionComponent: PlayerInteractionComponent) {
        val selectedEntity = interactionComponent.selectedEntity
        selectedEntity?.let { draw(selectedEntity, selectionEntitySprite) }

        val selectedTiles = interactionComponent.selectedTiles
        selectedTiles.forEach {
            draw(it, selectionTileSprite)
        }
    }

    //todo highlight sprite instead
    private fun drawHover(interactionComponent: PlayerInteractionComponent) {
        val hoveredEntity = interactionComponent.hoveredEntity ?: return
//        hoveredEntity[render]!!.switchSprite()
//        draw(hoveredEntity)
        draw(hoveredEntity, hoverSprite)
    }

    private fun draw(entity: Entity, sprite: Sprite) {
        val position = entity[position]!!.gridPosition
        draw(position, sprite)
    }

    private fun draw(position: GridPoint2, sprite: Sprite) {
        val region = sprite.textureRegion()

        batch.draw(vec2(position), region)
    }
}