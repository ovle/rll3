package com.ovle.rll3.model.ecs.system.gui

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.util.Mappers.living
import com.ovle.rll3.model.ecs.component.util.Mappers.render
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.model.ecs.system.EventSystem
import ktx.ashley.get


class GUISystem(private val stage: Stage, private val guiTexture: Texture) : EventSystem() {

    private val playerPanelInfo = EntityPanelInfo()
    private val focusedEntityPanelInfo = EntityPanelInfo()

    private var lastActionsPopup: Actor? = null
    private var lastEntityInfo: Actor? = null


    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)

        rootActor().addActor(entityInfoActor(playerPanelInfo, guiTexture))
    }

    override fun subscribe() {
        EventBus.subscribe<EntitySelectEvent> { onEntitySelectEvent(it.entity) }
        EventBus.subscribe<EntityDeselectEvent> { onEntityDeselectEvent() }
        EventBus.subscribe<EntityChanged> { onEntityChangedEvent(it.entity) }
        EventBus.subscribe<ShowEntityActionsEvent> { onShowEntityActionsEvent(it.entity, it.actions) }
    }

    private fun onEntitySelectEvent(entity: Entity) {
        onEntityDeselectEvent() //todo

        val actor = entityInfoActor(focusedEntityPanelInfo, guiTexture).apply {
            x = screenWidth() - width
        }

        rootActor().addActor(actor)
        lastEntityInfo = actor

        updateEntityInfo(entity, focusedEntityPanelInfo)
    }

    private fun onEntityDeselectEvent() {
        hideActionsPopup()
        hideFocusedEntityInfo()
    }

    private fun onEntityChangedEvent(entity: Entity) {
        val playerEntity = playerInteractionInfo()!!.controlledEntity!!
        if (entity == playerEntity) {
            updateEntityInfo(entity, playerPanelInfo)
        } else {
            if (entity == focusedEntityPanelInfo.entity) {
                updateEntityInfo(entity, focusedEntityPanelInfo)
            }
        }
    }

    //todo
    private fun onShowEntityActionsEvent(entity: Entity, actions: Collection<String>) {
        hideActionsPopup()
        val onActionClick:(String) -> Unit = {
            action ->
            hideActionsPopup()
            EventBus.send(EntityActionEvent(entity, action))
        }

        val actor = entityActionsActor(actions, entity, onActionClick)
                .apply {
                    lastActionsPopup = this
                    x = screenWidth()/2 - width/2
                }

        rootActor().addActor(actor)
    }

    private fun updateEntityInfo(entity: Entity, panelInfo: EntityPanelInfo) {
        val livingComponent = entity[living]
        val renderComponent = entity[render]
        val entityTemplate = entity[template]?.template

        with(panelInfo) {
            this.entity = entity

            val portraitOrigin = entityTemplate?.portrait?.random() //todo random ?
            val portrait = portraitOrigin?.run {
                val portraitSize = 24
                TextureRegion(guiTexture, x * portraitSize, y * portraitSize, portraitSize, portraitSize)
            } ?: renderComponent?.sprite?.textureRegion()

            portrait?.let {
                portraitWidget.drawable = TextureRegionDrawable(portrait)
            }

            val templateName = entity[template]?.template?.name ?: "no name"
            nameWidget.setText(templateName)

            if (livingComponent != null) {
                healthInfoWidget.setText("${livingComponent.health}/${livingComponent.maxHealth}")
                staminaInfoWidget.setText("${livingComponent.stamina}/${livingComponent.maxStamina}")
            }
        }
    }

    private fun rootActor() = stage.root
    private fun screenWidth() = 1017 //todo

    private fun hideActionsPopup() {
        rootActor().removeActor(lastActionsPopup)
        lastActionsPopup = null
    }

    private fun hideFocusedEntityInfo() {
        rootActor().removeActor(lastEntityInfo)
        lastEntityInfo = null
    }
}