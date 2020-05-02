package com.ovle.rll3.model.ecs.system.render

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion.split
import com.ovle.rll3.model.ecs.component.basic.RenderComponent
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.component.util.Mappers.animation
import com.ovle.rll3.model.ecs.component.util.Mappers.playerInteraction
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.Mappers.render
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.ecs.entity.playerInteraction
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.view.layer.TextureRegions
import com.ovle.rll3.view.sprite.Sprite
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import ktx.ashley.get


class RenderInteractionInfoSystem(
    private val batch: Batch,
    guiTexture: Texture
) : EntitySystem() {

    private val guiRegions = split(guiTexture, spriteWidth, spriteHeight)

    private val cursorSprite = sprite(guiRegions, 0, 0)
    private val selectionSprite = sprite(guiRegions, 1, 0)


    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        batch.begin()
        drawInteractionInfo(playerInteraction())
        batch.end()
    }

    private fun drawInteractionInfo(interactionEntity: Entity?) {
        if (interactionEntity == null) return

        val position = interactionEntity[position]!!.position
        batch.draw(position, cursorSprite.textureRegion())

        val pi = interactionEntity[playerInteraction]!!
        val selectedEntity = pi.selectedEntity
        if (selectedEntity != null) {
            val selectedPosition = selectedEntity[Mappers.position]!!.position
            batch.draw(selectedPosition, selectionSprite.textureRegion())
        }
    }
}