package com.ovle.rll3.model.ecs.system
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.Mappers.playerInteraction
import com.ovle.rll3.model.ecs.component.Mappers.position
import com.ovle.rll3.model.ecs.component.special.PlayerInteractionComponent
import com.ovle.rll3.model.ecs.entity.allEntities
import com.ovle.rll3.model.ecs.entity.entityWith
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.view.scaleScrollCoeff
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth
import ktx.ashley.get


class CameraSystem(
    private val camera: OrthographicCamera
): EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<CameraScaleInc> { onScaleChange(0.1f) }
        EventBus.subscribe<CameraScaleDec> { onScaleChange(-0.1f) }
        EventBus.subscribe<CameraScrolled> { onScaleChange(-it.amount.toFloat() * scaleScrollCoeff) }
        EventBus.subscribe<CameraMoved> { onScrollOffsetChange(it.amount) }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        focusCamera()
    }

    private fun focusCamera() {
        val interactionEntity = entityWith(allEntities().toList(), PlayerInteractionComponent::class)
            ?: return
        val interactionComponent = interactionEntity[playerInteraction] ?: return
        val focusedEntity = interactionComponent.focusedEntity ?: return
        val focusedPosition = focusedEntity[position]?.position ?: return
        val focusedScreenPosition = floatPoint(
            focusedPosition.x * tileWidth,
            focusedPosition.y * tileHeight
        )

        if (focusedScreenPosition.epsilonEquals(RenderConfig.scrollOffset)) return

        RenderConfig.scrollOffset = focusedScreenPosition
        camera.position.set(RenderConfig.scrollOffset.x, RenderConfig.scrollOffset.y, 0.0f)
        camera.update()
    }

    private fun onScaleChange(diff: Float) {
        camera.zoom -= diff
        camera.update()
    }

    private fun onScrollOffsetChange(diff: Vector2) {
        val scrollOffset = RenderConfig.scrollOffset
        scrollOffset.add(-diff.x, diff.y)
        camera.position.set(scrollOffset.x, scrollOffset.y, 0.0f)
        camera.update()
    }
}