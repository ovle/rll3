package com.ovle.rll3.model.ecs.system

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.special.SelectionMode
import com.ovle.rll3.model.ecs.component.special.SelectionMode.Entity
import com.ovle.rll3.model.ecs.component.special.SelectionMode.Tile
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.ecs.entity.*
import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.view.noVisibilityFilter
import com.ovle.rll3.view.viewportToGame
import ktx.ashley.get

//todo ambiguity - move vs entity action vs skill - all on left button, no way to order/separate using events for now
class PlayerControlsSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<MouseClick> {
            val gamePoint = it.viewportPoint.viewportToGame()
            onMouseClick(gamePoint, it.button)
        }
        EventBus.subscribe<MouseMoved> {
            val gamePoint = it.viewportPoint.viewportToGame()
            onMouseMoved(gamePoint)
        }
        EventBus.subscribe<NumKeyPressed> { onNumKeyPressed(it.number) }
        EventBus.subscribe<KeyPressed> { onKeyPressed(it.code) }
    }

    private fun onMouseClick(position: GridPoint2, button: Int) {
        val level = levelInfoNullable() ?: return

        send(Click(button, position))

        val controlledEntity = controlledEntity()

        val skillTemplate = selectedSkillTemplate()
        val skillWithTarget = skillTemplate?.target != null
        val skillTarget = skillTemplate?.target?.invoke(position, level)
        val isCorrectTarget = !skillWithTarget || skillTarget != null

        if (false) {
//        if (skillTemplate != null && isCorrectTarget) {
            send(EntityUseSkill(controlledEntity!!, skillTarget, skillTemplate!!))
        } else {
            val entities = level.entities.on(position)
//            val partyEntities = player()!!.party.entities.toList().on(position)
            when {
                entities.isNotEmpty() -> send(EntityClick(button, entities.single()))
//                partyEntities.isNotEmpty() -> send(EntityClick(button, partyEntities.single()))
                else -> {
//                    send(EntitySetMoveTarget(controlledEntity, position))
                    send(VoidClick(button, position))
                }
            }
        }
    }

    private fun onMouseMoved(position: GridPoint2) {
        val level = levelInfoNullable() ?: return
        val interactionEntity = playerInteraction(this.allEntities().toList()) ?: return
        val positionComponent = interactionEntity[Mappers.position]!!
        if (positionComponent.gridPosition == position) return

        positionComponent.gridPosition = position

        val entities = level.entities.on(position)
//        val partyEntities = player()!!.party.entities.toList().on(position)

        send(EntityUnhoverEvent())
        entities.forEach {
            send(EntityHoverEvent(it))
        }
//        partyEntities.forEach {
//            send(EntityHoverEvent(it))
//        }
    }

    private fun onNumKeyPressed(number: Int) {
        val controlledEntity = controlledEntity() ?: return
        val templateComponent = controlledEntity[template] ?: return
        val skillNames = templateComponent.template.skills
        if (number >= skillNames.size) return

        val selectedSkillName = skillNames[number]

        //todo
        val selectedSkillTemplate = TemplatesRegistry.skillTemplates[selectedSkillName]
        checkNotNull(selectedSkillTemplate)

        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.selectedSkillTemplate = selectedSkillTemplate

        println("select skill: $selectedSkillName")
        send(SkillSelected(selectedSkillTemplate))
    }

    private fun switchSelectionMode() {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.selectionMode = if (interactionInfo.selectionMode == Entity) Tile else Entity
    }

    private fun onKeyPressed(code: Int) {
        when(code) {
            //debug
            Input.Keys.ESCAPE -> send(
                DebugSaveGameEvent().then(ExitGameEvent())
            )
            Input.Keys.A -> send(DebugCombatEvent())
            Input.Keys.F -> send(DebugToggleFocusEvent())
            Input.Keys.I -> send(DebugShowPlayerInventoryEvent())
            Input.Keys.S -> switchSelectionMode()
            Input.Keys.V -> { noVisibilityFilter = !noVisibilityFilter }
        }
    }
}