package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.Mappers.render
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.roundToClosestByAbsInt
import com.ovle.rll3.view.layer.imageFromTexture
import com.ovle.rll3.view.tileWidth
import ktx.ashley.get
import ktx.scene2d.label
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
        val root = stage.root
        root.removeActor(last)
    }

    //todo
    private fun onEntitySelectEvent(entity: Entity) {
        val root = stage.root
        root.removeActor(last)

        //todo position
        val scale = RenderConfig.scale
        val entityPosition = entity[position]!!.position
        val screenX = (entityPosition.x * scale).roundToClosestByAbsInt() * tileWidth
        val screenY = (entityPosition.y * scale).roundToClosestByAbsInt() * tileWidth

        //todo works before resize
        val actorPosition = Vector2(screenX.toFloat(), screenY.toFloat())   //RenderConfig.unproject!!.invoke(Vector3(entityPosition, 0.0f))
        val templateName = entity[template]?.template?.name ?: "no name"
        val portrait = entity[render]?.portrait
        val desc = entity[template]?.template?.description ?: "no description"

        //todo available actions by components
        val actions = arrayOf("action1", "action2")

        root.addActor(
            window(templateName) {
                x = actorPosition.x
                y = actorPosition.y

                verticalGroup {
                    if (portrait != null) imageFromTexture(portrait.textureRegion())

                    label(desc)
                    actions.map {
                        textButton(text = it) {
//                            onClick { screenManager.goToScreen(ScreenManager.ScreenType.MainMenuScreenType) }
                        }
                    }
                }

                last = this
                pack()
            }
        )
    }
}
