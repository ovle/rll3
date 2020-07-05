package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.util.Mappers.questOwner
import com.ovle.rll3.model.ecs.entity.controlledEntity
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.model.ecs.entity.selectedEntity
import com.ovle.rll3.model.ecs.system.EventSystem
import com.ovle.rll3.model.ecs.system.interaction.EntityInteractionType.*
import com.ovle.rll3.model.ecs.system.interaction.skill.SkillTemplate
import com.ovle.rll3.model.ecs.system.interaction.skill.skill
import com.ovle.rll3.model.ecs.system.interaction.use.use
import ktx.ashley.get

//todo move skills out
//todo move technical stuff out?
class EntityInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntityClick> { onEntityClickEvent(it.entity, it.button) }
        EventBus.subscribe<VoidClick> { onVoidClickEvent(it.button, it.point) }
        EventBus.subscribe<EntityHoverEvent> { onEntityHoverEvent(it.entity) }
        EventBus.subscribe<EntityUnhoverEvent> { onEntityUnhoverEvent() }

        EventBus.subscribe<EntityInteractionEvent> { onEntityActionEvent(it.entity, it.interaction) }
        EventBus.subscribe<EntityUseSkill> { onEntityUseSkillEvent(it.entity, it.target, it.skillTemplate) }
    }

    private fun onEntityClickEvent(entity: Entity, button: Int) {
        select(entity)

        when (button) {
            Buttons.RIGHT -> {
                showActions(entity)
            }
            else -> {
                val defaultInteractionType = defaultInteraction(entity)
                if (defaultInteractionType == null) {
                    showActions(entity)
                } else {
                    performEntityInteraction(entity, EntityInteraction(defaultInteractionType))
                }
            }
        }
    }

    private fun onVoidClickEvent(button: Int, point: GridPoint2) {
        deselect()
        send(HideEntityActionsEvent())
    }

    private fun onEntityHoverEvent(entity: Entity) {
        if (selectedEntity() != null) return

        send(ShowEntityInfoEvent(entity))
    }

    private fun onEntityUnhoverEvent() {
        if (selectedEntity() != null) return

        send(HideEntityInfoEvent())
    }

    private fun select(entity: Entity) {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.selectedEntity = entity
    }

    private fun deselect() {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.selectedEntity = null
    }

    private fun onEntityActionEvent(entity: Entity, interaction: EntityInteraction) {
        performEntityInteraction(entity, interaction)
    }

    private fun performEntityInteraction(entity: Entity, interaction: EntityInteraction) {
        val playerEntity = controlledEntity()!!

        when (interaction.type) {
            Travel -> {
                val connectionComponent = entity[levelConnection]!!
                send(EntityLevelTransition(playerEntity, connectionComponent.id))
            }
            Talk -> showTalkActions(playerEntity, entity)
            Use -> {
                use(playerEntity, entity)
                //todo events
            }
        }
    }

    //todo
    private fun onEntityUseSkillEvent(entity: Entity, target: Any?, skillTemplate: SkillTemplate) {
        println("$entity use skill ${skillTemplate.name} on $target")
        skill(entity, target, skillTemplate)
    }

    private fun showActions(entity: Entity) {
        val controlledEntity = controlledEntity()
        val interactions = availableInteractions(entity) //.filter { isAvailable(it, controlledEntity!!, entity) }
        if (interactions.isNotEmpty()) {
            showActions(entity, interactions)
        }
    }

    private fun showTalkActions(playerEntity: Entity, entity: Entity) {
        //todo dialogs
        //todo check available/taken quests
        val actions = entity[questOwner]?.questIds ?: mutableListOf()
        if (actions.isNotEmpty()) {
            //todo
//            showActions(entity, actions)
        }
    }

    private fun showActions(entity: Entity, interactions: List<EntityInteractionType>) {
        send(ShowEntityActionsEvent(entity, interactions))
    }
}
