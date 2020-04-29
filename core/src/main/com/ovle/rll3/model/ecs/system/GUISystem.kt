package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.util.Mappers.living
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.view.layer.image
import ktx.actors.onClick
import ktx.ashley.get
import ktx.scene2d.*
import com.ovle.rll3.view.layer.label as cLabel


class GUISystem(private val stage: Stage, private val guiTexture: Texture) : EventSystem() {

    private var last: Actor? = null

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)

        stage.addActor(playerInfoActor())
    }

    override fun subscribe() {
        EventBus.subscribe<Event.EntityUnselectEvent> { onEntityUnselectEvent() }
        EventBus.subscribe<Event.EntityChanged> { onEntityChangedEvent(it.entity) }
        EventBus.subscribe<Event.ShowEntityActionsEvent> { onShowEntityActionsEvent(it.entity, it.actions) }
    }

    private fun onEntityUnselectEvent() {
        hideActionsPopup()
    }

    private fun onEntityChangedEvent(entity: Entity) {
        val playerEntity = playerInteractionInfo()!!.controlledEntity!!
        if (entity != playerEntity) return

        updateEntityInfo(entity, playerPanelInfo)
    }

    private fun updateEntityInfo(entity: Entity, panelInfo: EntityPanelInfo) {
        with(panelInfo) {
            val entityTemplate = entity[template]?.template
            val portraitOrigin = entityTemplate?.portrait?.random() //todo random ?
            portraitOrigin?.let {
                val portraitSize = 24
                val portrait = TextureRegion(guiTexture, it.x * portraitSize, it.y * portraitSize, portraitSize, portraitSize)
                portraitWidget.drawable = TextureRegionDrawable(portrait)
            }
            val livingComponent = entity[living]!!
            val templateName = entity[template]?.template?.name ?: "no name"

            nameWidget.setText(templateName)
            healthInfoWidget.setText("${livingComponent.health}/${livingComponent.maxHealth}")
            staminaInfoWidget.setText("${livingComponent.stamina}/${livingComponent.maxStamina}")
        }
    }

    //todo
    private fun onShowEntityActionsEvent(entity: Entity, actions: Collection<String>) {
        hideActionsPopup()

        val livingComponent = entity[living]
        val templateName = entity[template]?.template?.name ?: "no name"
//        val portrait = entity[render]?.portrait
//        val desc = entity[template]?.template?.description

        val root = stage.root
        root.addActor(
            window(
                title = templateName,
                style = "default"
            ) {
                bottom()

                if (livingComponent != null) {
                    row()
                    label("health: ${livingComponent.health}").cell(align = Align.left, padLeft = 10.0f, padTop = 10.0f)
                    row()
                    label("stamina: ${livingComponent.stamina}").cell(align = Align.left, padLeft = 10.0f, padTop = 10.0f)
                    row()
                }

                verticalGroup {
                    actions.mapIndexed {
                        i, action ->
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

    private fun hideActionsPopup() {
        val root = stage.root
        root.removeActor(last)
    }

    //todo
    class EntityPanelInfo {
        lateinit var portraitWidget: Image
        lateinit var nameWidget: Label

        lateinit var healthInfoWidget: Label
        lateinit var staminaInfoWidget: Label
    }
    private val playerPanelInfo = EntityPanelInfo()

    //todo refactor
    fun playerInfoActor(): Actor {
        val scale = 4.0f
        val guiTexture = guiTexture
        val portrait = TextureRegion(guiTexture, 0, 24, 24, 24)
        val bg = TextureRegion(guiTexture, 120, 0, 72, 40)
        val pi = image(portrait).also { playerPanelInfo.portraitWidget = it }

        val leftPart = table {
            add(pi).size(24 * scale, 24 * scale)
                    .padRight(2.0f * scale).padBottom(1.0f * scale)
            row()
            add(cLabel().also { playerPanelInfo.nameWidget = it }).padTop(2 * scale)
        }

        val rightPart = table {
            defaults().padBottom(5 * scale).padLeft(10 * scale)

            add(
                    cLabel().also { playerPanelInfo.healthInfoWidget = it }
            ).padTop(6 * scale)
            row()
            label("00/00")
            row()
            add(
                    cLabel().also { playerPanelInfo.staminaInfoWidget = it }
            )
            row()
            label("00/00")
        }

        val percentWidth50 = Value.percentWidth(50.0f)
        val result = table {
            height = bg.regionHeight * scale
            width = bg.regionWidth * scale
            background = TextureRegionDrawable(bg)

            add(leftPart).width(percentWidth50).expand()
            add(rightPart).width(percentWidth50).expand()
        }

        return result
    }
}