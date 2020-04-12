package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.roundToClosestByAbsInt
import com.ovle.rll3.toMapScreenPoint
import com.ovle.rll3.view.tileWidth
import ktx.ashley.get
import ktx.scene2d.container
import ktx.scene2d.label


class GUISystem(private val stage: Stage) : EventSystem() {

    private var last: Actor? = null

    override fun subscribe() {
        EventBus.subscribe<Event.EntitySelectEvent> { onEntitySelectEvent(it.entity) }
    }

    private fun onEntitySelectEvent(entity: Entity) {
        val root = stage.root
        root.removeActor(last)

        val scale = RenderConfig.scale
        val entityPosition = entity[position]!!.position
        val screenX = (entityPosition.x * scale).roundToClosestByAbsInt() * tileWidth
        val screenY = (entityPosition.y * scale).roundToClosestByAbsInt() * tileWidth

        val globalPosition = toMapScreenPoint(entityPosition, RenderConfig)

        val actorPosition = Vector2(screenX.toFloat(), screenY.toFloat())   //RenderConfig.unproject!!.invoke(Vector3(entityPosition, 0.0f))
        val name = entity[template]?.template?.name ?: "no name"
        root.addActor(
            container {
                x = actorPosition.x
                y = actorPosition.y

                label(text = name)

                last = this
                pack()
            }
        )
    }
}
