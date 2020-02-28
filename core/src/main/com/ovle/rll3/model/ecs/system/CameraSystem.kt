package com.ovle.rll3.model.ecs.system
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.Event
import com.ovle.rll3.Event.*
import com.ovle.rll3.EventBus
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.PlayerInteractionComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.componentMapper
import com.ovle.rll3.model.ecs.entity.EntityQuery.entityWithNullable
import com.ovle.rll3.model.ecs.entity.allEntities
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.view.scaleScrollCoeff
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth
import ktx.ashley.get


class CameraSystem(
    private val camera: OrthographicCamera
): EventSystem<Event>() {

    private val position: ComponentMapper<PositionComponent> = componentMapper()
    private val interaction: ComponentMapper<PlayerInteractionComponent> = componentMapper()

    override fun channel() = EventBus.receive<Event>()

    override fun dispatch(event: Event) {
        when (event) {
            is CameraScaleInc -> onScaleChange(0.1f)
            is CameraScaleDec -> onScaleChange(-0.1f)
            is CameraScrolled -> onScaleChange(-event.amount.toFloat() * scaleScrollCoeff)
            is CameraMoved -> onScrollOffsetChange(event.amount)
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        focusCamera()
    }

    private fun focusCamera() {
        val interactionEntity = entityWithNullable(allEntities().toList(), PlayerInteractionComponent::class)
            ?: return
        val interactionComponent = interactionEntity[interaction] ?: return
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