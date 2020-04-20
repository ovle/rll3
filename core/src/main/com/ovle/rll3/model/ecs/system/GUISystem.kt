package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion.split
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.Mappers.render
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.point
import com.ovle.rll3.roundToClosestByAbsInt
import com.ovle.rll3.view.layer.imageFromTexture
import com.ovle.rll3.view.tileWidth
import ktx.actors.onClick
import ktx.ashley.get
import ktx.scene2d.*


class GUISystem(private val stage: Stage, private val guiTexture: Texture) : EventSystem() {

    companion object {
        private val smallGuiElementSize = 12
        private val largeGuiElementSize = 24
        private val smallGuiElements = mapOf(
            "health" to point(0, 2 ),
            "energy" to point(0, 3 ),
            "money" to point(1, 2 ),
            "stamina" to point(1, 3 )
        )
    }

    private val smallTextureElements = split(guiTexture, smallGuiElementSize, smallGuiElementSize)
    private val largeTextureElements = split(guiTexture, largeGuiElementSize, largeGuiElementSize)

    private var last: Actor? = null


    override fun subscribe() {
        EventBus.subscribe<Event.EntityUnselectEvent> { onEntityUnselectEvent() }
        EventBus.subscribe<Event.ShowEntityActionsEvent> { onShowEntityActionsEvent(it.entity, it.actions) }
    }

    private fun onEntityUnselectEvent() {
        hideActionsPopup()
    }

    //todo
    private fun onShowEntityActionsEvent(entity: Entity, actions: Collection<String>) {
        hideActionsPopup()

        //todo position
        val scale = RenderConfig.scale
        val entityPosition = entity[position]!!.position
        val screenX = (entityPosition.x * scale).roundToClosestByAbsInt() * tileWidth
        val screenY = (entityPosition.y * scale).roundToClosestByAbsInt() * tileWidth

        val livingComponent = entity[Mappers.living]

        //todo works before resize
        val actorPosition = Vector2(screenX.toFloat(), screenY.toFloat())   //RenderConfig.unproject!!.invoke(Vector3(entityPosition, 0.0f))
        val templateName = entity[template]?.template?.name ?: "no name"
        val portrait = entity[render]?.portrait
        val desc = entity[template]?.template?.description

        val root = stage.root
        root.addActor(
            window(
                title = templateName,
                style = "default"
            ) {
                x = actorPosition.x
                y = actorPosition.y

                if (livingComponent != null) {
                    row()
                    label("health: ${livingComponent.health}").cell(align = Align.left, padLeft = 10.0f, padTop = 10.0f)
                    row()
                    label("stamina: ${livingComponent.stamina}").cell(align = Align.left, padLeft = 10.0f, padTop = 10.0f)
                    row()
                }

                verticalGroup {
                    actions.map {
                        action ->
                        textButton(text = action) {
                            onClick {
                                hideActionsPopup()
                                EventBus.send(Event.EntityActionEvent(entity, action))
                            }
                        }
                    }
                }.cell(pad = 10.0f)

                last = this
                pack()
            }
        )
    }

    private fun image(imageName: String, kWindow: KWindow): Actor {
        val coords = smallGuiElements.getValue(imageName)
        val textureRegion = smallTextureElements[coords.x][coords.y]
        return kWindow.imageFromTexture(textureRegion)
    }

    private fun hideActionsPopup() {
        val root = stage.root
        root.removeActor(last)
    }
}