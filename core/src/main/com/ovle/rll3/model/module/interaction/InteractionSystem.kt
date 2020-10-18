package com.ovle.rll3.model.module.interaction

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.Event.PlayerControlEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.entity.*
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.skill.SkillTemplate
import ktx.ashley.get

/**
 * player's interaction with entities (low-level)
 */
class InteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntityClickEvent> { onEntityClickEvent(it.entity, it.button) }
        EventBus.subscribe<VoidClickEvent> { onVoidClickEvent(it.button, it.point) }
        EventBus.subscribe<EntityHoverEvent> { onEntityHoverEvent(it.entity) }
        EventBus.subscribe<EntityUnhoverEvent> { onEntityUnhoverEvent() }
        EventBus.subscribe<SkillSelectCommand> { onSkillSelectCommand(it.skill) }

        EventBus.subscribe<EntityDestroyedEvent> { onEntityDestroyedEvent(it.entity) }
    }

    private fun onEntityDestroyedEvent(entity: Entity) {
        val interactionInfo = playerInteractionInfo()!!
        with(interactionInfo) {
            if (selectedEntity == entity) selectedEntity = null
            if (focusedEntity == entity) focusedEntity = null
            if (hoveredEntity == entity) hoveredEntity = null
        }
    }

    private fun onEntityClickEvent(entity: Entity, button: Int) {
        val interactionInfo = playerInteractionInfo()!!
        when {
            partyEntitySelected(interactionInfo) &&
            skillSelected(interactionInfo) &&
            skillCanTargetEntity(interactionInfo, entity) -> selectEntitySkillTarget(interactionInfo, entity)
//            !entitySelected(interactionInfo) -> select(entity)
//            !partyEntitySelected(interactionInfo) -> select(entity)
//            !skillSelected(interactionInfo) -> select(entity)
//            !skillCanTargetEntity(interactionInfo, entity) -> {}
//            !skillTargetSelected(interactionInfo) -> {}
//            !skillTargetIsEntity(interactionInfo, entity) -> {}
            else -> select(entity)
        }
    }

    private fun onVoidClickEvent(button: Int, point: GridPoint2) {
        val interactionInfo = playerInteractionInfo()!!
        when {
            partyEntitySelected(interactionInfo) &&
            skillSelected(interactionInfo) &&
            skillCanTargetTile(interactionInfo, point) -> selectTileSkillTarget(interactionInfo, point)
            else -> { }
        }
    }

    private fun selectTileSkillTarget(interactionInfo: PlayerInteractionComponent, point: GridPoint2) {
        val skill = interactionInfo.selectedSkill!!

        val locationInfo = locationInfo()
        if (!locationInfo.tiles.isPointValid(point)) return

        val targetCandidate = skill.target?.invoke(point, locationInfo)
        check(targetCandidate is GridPoint2)

        interactionInfo.selectedSkillTarget = targetCandidate

        println("skill target: $point")
    }

    private fun selectEntitySkillTarget(interactionInfo: PlayerInteractionComponent, entity: Entity) {
        val skill = interactionInfo.selectedSkill!!

        val locationInfo = locationInfo()
        val entityPosition = entity[position]!!.gridPosition
        val targetCandidate = skill.target?.invoke(entityPosition, locationInfo)
        check(targetCandidate is Entity)

        interactionInfo.selectedSkillTarget = targetCandidate

        println("skill target: $entity")
    }


    private fun onSkillSelectCommand(skill: SkillTemplate) {
        val interactionInfo = playerInteractionInfo()!!
        check(partyEntitySelected(interactionInfo))

        interactionInfo.selectedSkill = skill
        interactionInfo.selectedSkillTarget = null

        val selectedEntity = interactionInfo.selectedEntity!!
        selectedEntity[ComponentMappers.action]!!.selectedSkill = skill

//        send(EntitySkillSelectedEvent(selectedEntity()!!, skill))
    }

    private fun onEntityHoverEvent(entity: Entity) {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.hoveredEntity = entity

        send(ShowEntityInfoCommand(entity))
    }

    private fun onEntityUnhoverEvent() {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.hoveredEntity = null

        send(HideEntityInfoCommand())
    }

    private fun select(entity: Entity) {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.selectedEntity = entity
    }

    private fun deselect() {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.selectedEntity = null
    }

    private fun entitySelected(interactionInfo: PlayerInteractionComponent) = interactionInfo.selectedEntity != null
    private fun partyEntitySelected(interactionInfo: PlayerInteractionComponent) = interactionInfo.selectedEntity?.id() in gameInfo()!!.party
    private fun skillSelected(interactionInfo: PlayerInteractionComponent) = interactionInfo.selectedSkill != null
    private fun skillCanTargetEntity(interactionInfo: PlayerInteractionComponent, entity: Entity) = interactionInfo.selectedSkill?.canTargetEntity(entity) ?: false
    private fun skillCanTargetTile(interactionInfo: PlayerInteractionComponent, point: GridPoint2) = interactionInfo.selectedSkill?.canTargetTile(point) ?: false
    private fun skillTargetSelected(interactionInfo: PlayerInteractionComponent) = interactionInfo.selectedSkillTarget != null
    private fun skillTargetIsEntity(interactionInfo: PlayerInteractionComponent, entity: Entity) = interactionInfo.selectedSkillTarget == entity

    private fun SkillTemplate.canTargetEntity(entity: Entity) = target?.invoke(entity[position]!!.gridPosition, locationInfo()) == entity
    private fun SkillTemplate.canTargetTile(point: GridPoint2) = target?.invoke(point, locationInfo()) == point
}
