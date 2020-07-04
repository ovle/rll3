package com.ovle.rll3.model.ecs.system.render

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion.split
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.util.Mappers.playerInteraction
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.entity.allEntities
import com.ovle.rll3.model.ecs.entity.playerInteraction
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import ktx.ashley.get


class RenderInteractionInfoSystem(
    private val batch: Batch,
    guiTexture: TexturesInfo
) : EntitySystem() {

    private val guiRegions = split(guiTexture.texture, spriteWidth.toInt(), spriteHeight.toInt())

    private val cursorSprite = sprite(guiRegions, 0, 0)
    private val selectionSprite = sprite(guiRegions, 1, 0)


    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        batch.begin()
        drawInteractionInfo(playerInteraction(this.allEntities().toList()))
        batch.end()
    }

    private fun drawInteractionInfo(interactionEntity: Entity?) {
        if (interactionEntity == null) return

        val gridPosition = interactionEntity[position]!!.gridPosition
        batch.draw(floatPoint(gridPosition), cursorSprite.textureRegion())

        val pi = interactionEntity[playerInteraction]!!
        val selectedEntity = pi.selectedEntity
        if (selectedEntity != null) {
            val selectedPosition = selectedEntity[position]!!.gridPosition
            batch.draw(floatPoint(selectedPosition), selectionSprite.textureRegion())
        }
    }
}