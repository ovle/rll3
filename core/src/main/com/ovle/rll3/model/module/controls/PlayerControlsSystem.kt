package com.ovle.rll3.model.module.controls

import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.Event.GameEvent.*
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

        val entities = location.entities.on(position)
        when {
            entities.isNotEmpty() -> send(EntityClickEvent(button, entities.first()))
            else -> {
//                    send(EntitySetMoveTarget(controlledEntity, position))
                send(VoidClickEvent(button, position))
            }
        }
        send(ClickEvent(button, position))
    }

    private fun onMouseMoved(position: GridPoint2) {
        val level = locationInfoNullable() ?: return
        val interactionEntity = playerInteraction(this.allEntities().toList()) ?: return
        val positionComponent = interactionEntity[ComponentMappers.position]!!
        if (positionComponent.gridPosition == position) return

        send(EntityUnhoverEvent())

        positionComponent.gridPosition = position
        val entities = level.entities.on(position)
        entities.forEach {
            send(EntityHoverEvent(it))
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
        val selectedEntity = selectedEntity()
        when(code) {
            ESCAPE -> send(
                DebugSaveGame().then(ExitGameCommand())
            )
            S -> send(DebugSwitchSelectionMode(interactionComponent.selectionMode.next()))
            C -> send(DebugSwitchControlMode(interactionComponent.controlMode.next()))
            Z -> send(CancelAllTasksCommand())
            K -> selectedEntity?.let { send(KillEntityCommand(it)) }
            R -> selectedEntity?.let { send(ResurrectEntityCommand(it)) }
            D -> selectedEntity?.let { send(DestroyEntityCommand(it)) }
            V -> { noVisibilityFilter = !noVisibilityFilter }
            LEFT_BRACKET -> send(DecGameSpeedCommand())
            RIGHT_BRACKET -> send(IncGameSpeedCommand())
            SPACE -> send(SwitchPauseGameCommand())
        }
    }
}