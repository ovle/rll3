package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.basic.TaskPerformerComponent
import com.ovle.rll3.model.ecs.component.special.ControlMode
import com.ovle.rll3.model.ecs.component.special.SelectionMode
import com.ovle.rll3.model.ecs.component.util.Mappers.questOwner
import com.ovle.rll3.model.ecs.component.util.Mappers.taskPerformer
import com.ovle.rll3.model.ecs.component.util.has
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.model.ecs.system.EventSystem
import com.ovle.rll3.model.ecs.system.interaction.skill.SkillTemplate
import ktx.ashley.get
import ktx.ashley.has

//todo move skills out
//todo move technical stuff out?
class EntityInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntityClick> { onEntityClickEvent(it.entity, it.button) }
        EventBus.subscribe<VoidClick> { onVoidClickEvent(it.button, it.point) }
        EventBus.subscribe<EntityHover> { onEntityHoverEvent(it.entity) }
        EventBus.subscribe<EntityUnhover> { onEntityUnhoverEvent() }

        EventBus.subscribe<Event.EntityInteraction> { onEntityActionEvent(it.entity, it.interaction) }
        EventBus.subscribe<EntityUseSkill> { onEntityUseSkillEvent(it.entity, it.target, it.skillTemplate) }
    }

    private fun onEntityClickEvent(entity: Entity, button: Int) {
        val interactionInfo = playerInteractionInfo()!!
        if (interactionInfo.selectionMode != SelectionMode.Entity) return

        select(entity)

        val isStartTask = interactionInfo.controlMode == ControlMode.Task && entity.has<TaskPerformerComponent>()
        if (isStartTask) {
//            entity[taskPerformer]!!.current = testTask  //todo
        }
    }

    private fun onVoidClickEvent(button: Int, point: GridPoint2) {
        deselect()
        send(HideEntityActions())
    }

    private fun onEntityHoverEvent(entity: Entity) {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.hoveredEntity = entity

        send(ShowEntityInfo(entity))
    }

    private fun onEntityUnhoverEvent() {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.hoveredEntity = null

        send(HideEntityInfo())
    }

    private fun select(entity: Entity) {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.selectedEntity = entity
    }

    //at least one party entity is already selected?
    private fun deselect() {
        val interactionInfo = playerInteractionInfo()!!

        interactionInfo.selectedEntity = null
    }

    private fun onEntityActionEvent(entity: Entity, interaction: EntityInteraction) {
        performEntityInteraction(entity, interaction)
    }

    private fun performEntityInteraction(entity: Entity, interaction: EntityInteraction) {
//        val playerEntity = controlledEntity()!!
//
//        when (interaction.type) {
//            Travel -> {
//                val connectionComponent = entity[levelConnection]!!
//                send(EntityLevelTransition(playerEntity, connectionComponent.id))
//            }
//            Talk -> showTalkActions(playerEntity, entity)
//            Use -> {
//                use(playerEntity, entity)
//                //todo events
//            }
//        }
    }

    //todo
    private fun onEntityUseSkillEvent(entity: Entity, target: Any?, skillTemplate: SkillTemplate) {
        println("$entity use skill ${skillTemplate.name} on $target")
//        skill(entity, target, skillTemplate)
    }

    private fun showActions(entity: Entity) {
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
        send(ShowEntityActions(entity, interactions))
    }
}
