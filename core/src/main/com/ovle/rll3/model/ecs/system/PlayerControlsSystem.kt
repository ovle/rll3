package com.ovle.rll3.model.ecs.system

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.util.Mappers.playerInteraction
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.ecs.entity.*
import com.ovle.rll3.model.ecs.see
import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.point
import com.ovle.rll3.toGamePoint
import com.ovle.rll3.view.noVisibilityFilter
import ktx.ashley.get

//todo ambiguity - move vs entity action vs skill - all on left button, no way to order/separate using events for now
class PlayerControlsSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<MouseClick> { onMouseClick(it, it.button) }
        EventBus.subscribe<MouseMoved> { onMousePositionChange(toGamePoint(it.screenPoint, RenderConfig)) }
        EventBus.subscribe<NumKeyPressed> { onNumKeyPressed(it.number) }
        EventBus.subscribe<KeyPressed> { onKeyPressed(it.code) }
    }

    private fun onMouseClick(event: MouseClick, button: Int) {
        val level = levelInfoNullable() ?: return

        val gamePoint = centered(toGamePoint(event.screenPoint, RenderConfig))
        val position = point(gamePoint)

        val controlledEntity = controlledEntity()
        if (controlledEntity?.see(position) != true) return

        val skillTemplate = selectedSkillTemplate()
        val skillWithTarget = skillTemplate?.target != null
        val skillTarget = skillTemplate?.target?.invoke(position, level)
        val isCorrectTarget = !skillWithTarget || skillTarget != null

        if (skillTemplate != null && isCorrectTarget) {
            send(EntityUseSkill(controlledEntity, skillTarget, skillTemplate))
        } else {
            val entities = level.entities.on(position)
            when {
                entities.isNotEmpty() -> send(EntityClick(button, entities.single()))
                else -> {
                    send(EntitySetMoveTarget(controlledEntity, position))
                    send(VoidClick(button, position))
                }
            }
        }
    }

    private fun onMousePositionChange(gamePoint: Vector2) {
        val level = levelInfoNullable() ?: return

        val point = point(centered(gamePoint.cpy()))
        val tiles = level.tiles
        if (!tiles.isPointValid(point.x, point.y)) return

        val interactionEntity = playerInteraction(this.allEntities().toList()) ?: return
        val positionComponent = interactionEntity[position]!!

        if (positionComponent.gridPosition == point) return

        positionComponent.gridPosition = point

        val controlledEntity = controlledEntity()
        if (controlledEntity?.see(positionComponent.gridPosition) == false) return

        val entitiesOnPosition = level.entities.on(positionComponent.gridPosition) //todo filter interaction itself
        val interactionComponent = interactionEntity[playerInteraction] ?: return
        interactionComponent.hoveredEntities = entitiesOnPosition

        send(EntityUnhoverEvent())
        entitiesOnPosition.forEach {
            send(EntityHoverEvent(it))
        }
    }

    private fun onNumKeyPressed(number: Int) {
        val controlledEntity = controlledEntity() ?: return
        val templateComponent = controlledEntity[template] ?: return
        val skillNames = templateComponent.template.skills
        if (number >= skillNames.size) return

        val selectedSkillName = skillNames[number]
        val selectedSkillTemplate = TemplatesRegistry.skillTemplates[selectedSkillName]
        checkNotNull(selectedSkillTemplate)

        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.selectedSkillTemplate = selectedSkillTemplate

        println("select skill: $selectedSkillName")
        send(SkillSelected(selectedSkillTemplate))
    }

    private fun onKeyPressed(code: Int) {
        when(code) {
            //debug
            Input.Keys.A -> send(DebugCombatEvent())
            Input.Keys.F -> send(DebugToggleFocusEvent())
            Input.Keys.I -> send(DebugShowPlayerInventoryEvent())
            Input.Keys.V -> { noVisibilityFilter = !noVisibilityFilter }
        }
    }

    private fun centered(gamePoint: Vector2) = floatPoint(gamePoint.x - 0.5f, gamePoint.y - 0.5f)
}
