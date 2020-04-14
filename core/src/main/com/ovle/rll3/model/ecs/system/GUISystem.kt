package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.actions
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.Mappers.render
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.roundToClosestByAbsInt
import com.ovle.rll3.view.tileWidth
import ktx.actors.onClick
import ktx.ashley.get
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup
import ktx.scene2d.window


class GUISystem(private val stage: Stage) : EventSystem() {

    private var last: Actor? = null

    override fun subscribe() {
        EventBus.subscribe<Event.EntityUnselectEvent> { onEntityUnselectEvent() }
        EventBus.subscribe<Event.EntitySelectEvent> { onEntitySelectEvent(it.entity) }
    }

    private fun onEntityUnselectEvent() {
        hideActionsPopup()
    }

    //todo
    private fun onEntitySelectEvent(entity: Entity) {
        hideActionsPopup()

        //todo position
        val scale = RenderConfig.scale
        val entityPosition = entity[position]!!.position
        val screenX = (entityPosition.x * scale).roundToClosestByAbsInt() * tileWidth
        val screenY = (entityPosition.y * scale).roundToClosestByAbsInt() * tileWidth

        //todo works before resize
        val actorPosition = Vector2(screenX.toFloat(), screenY.toFloat())   //RenderConfig.unproject!!.invoke(Vector3(entityPosition, 0.0f))
        val templateName = entity[template]?.template?.name ?: "no name"
        val portrait = entity[render]?.portrait
        val desc = entity[template]?.template?.description

        val interaction = actions(entity)

        val root = stage.root
        root.addActor(
            window(
                title = templateName,
                style = "default"
            ) {
                x = actorPosition.x
                y = actorPosition.y

                verticalGroup {
                    interaction.map {
                        interaction ->
                        textButton(text = interaction.name) {
                            onClick {
                                EventBus.send(Event.EntityInteractionEvent(entity, interaction))
                                hideActionsPopup()
                            }
                        }
                    }
                }.cell(pad = 10.0f)

                last = this
                pack()
            }
        )
    }

    private fun hideActionsPopup() {
        val root = stage.root
        root.removeActor(last)
    }
}