package com.ovle.rll3.model.module.controls

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.Event.PlayerControlEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.core.entity.*
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.next
import com.ovle.rll3.view.noVisibilityFilter
import com.ovle.rll3.view.viewportToGame
import ktx.ashley.get

//todo ambiguity - move vs entity action vs skill - all on left button, no way to order/separate using events for now
class PlayerControlsSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<MouseClickEvent> {
            val gamePoint = it.viewportPoint.viewportToGame()
            onMouseClick(gamePoint, it.button)
        }
        EventBus.subscribe<MouseMovedEvent> {
            val gamePoint = it.viewportPoint.viewportToGame()
            onMouseMoved(gamePoint)
        }
        EventBus.subscribe<NumKeyPressedEvent> { onNumKeyPressed(it.number) }
        EventBus.subscribe<KeyPressedEvent> { onKeyPressed(it.code) }
    }

    private fun onMouseClick(position: GridPoint2, button: Int) {
        val location = locationInfoNullable() ?: return

//        val skillTemplate = selectedSkillTemplate()
//        val skillWithTarget = skillTemplate?.target != null
//        val skillTarget = skillTemplate?.target?.invoke(position, level)
//        val isCorrectTarget = !skillWithTarget || skillTarget != null

        if (false) {
//        if (skillTemplate != null && isCorrectTarget) {
//            send(EntityUseSkill(controlledEntity!!, skillTarget, skillTemplate!!))
        } else {
            val entities = location.entities.on(position)
//            val partyEntities = player()!!.party.entities.toList().on(position)
            when {
                entities.isNotEmpty() -> send(GameEvent.EntityClickEvent(button, entities.single()))
//                partyEntities.isNotEmpty() -> send(EntityClick(button, partyEntities.single()))
                else -> {
//                    send(EntitySetMoveTarget(controlledEntity, position))
                    send(VoidClickEvent(button, position))
                }
            }
        }
        send(ClickEvent(button, position))
    }

    private fun onMouseMoved(position: GridPoint2) {
        val level = locationInfoNullable() ?: return
        val interactionEntity = playerInteraction(this.allEntities().toList()) ?: return
        val positionComponent = interactionEntity[ComponentMappers.position]!!
        if (positionComponent.gridPosition == position) return

        send(GameEvent.EntityUnhoverEvent())

        positionComponent.gridPosition = position
        val entities = level.entities.on(position)
        entities.forEach {
            send(GameEvent.EntityHoverEvent(it))
        }
    }

    private fun onNumKeyPressed(number: Int) {
//        val controlledEntity = controlledEntity() ?: return
//        val templateComponent = controlledEntity[template] ?: return
//        val skillNames = templateComponent.template.skills
//        if (number >= skillNames.size) return
//
//        val selectedSkillName = skillNames[number]
//
//        //todo
//        val selectedSkillTemplate = TemplatesRegistry.skillTemplates[selectedSkillName]
//        checkNotNull(selectedSkillTemplate)
//
//        val interactionInfo = playerInteractionInfo()!!
//        interactionInfo.selectedSkillTemplate = selectedSkillTemplate
//
//        println("select skill: $selectedSkillName")
//        send(SkillSelected(selectedSkillTemplate))
    }

    private fun onKeyPressed(code: Int) {
        val interactionComponent = playerInteractionInfo()!!
        when(code) {
            //debug
            Input.Keys.ESCAPE -> send(
                DebugSaveGame().then(ExitGameCommand())
            )
            Input.Keys.S -> send(DebugSwitchSelectionMode(interactionComponent.selectionMode.next()))
            Input.Keys.C -> send(DebugSwitchControlMode(interactionComponent.controlMode.next()))
            Input.Keys.V -> { noVisibilityFilter = !noVisibilityFilter }
        }
    }
}