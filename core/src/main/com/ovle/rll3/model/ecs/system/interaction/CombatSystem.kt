package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.advanced.LivingComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.living
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.model.ecs.system.EventSystem
import ktx.ashley.get
import java.lang.Integer.min


class CombatSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<Event.EntityActionEvent> { onEntityInteractionEvent(it.entity, it.action) }
    }

    //todo rewrite to processors
    private fun onEntityInteractionEvent(entity: Entity, action: String) {
        val playerEntity = playerInteractionInfo()!!.controlledEntity!!

        when (action) {
            in combatActionNames -> {
                val source = playerEntity   //todo not always
                val target = entity
                val chosenAction = combatActions.single { it.name == action }
                val targetChosenAction = combatActions.filter { isAvailable(it, target, source) }.random() //todo use ai

                send(Event.EntityCombatAction(source, chosenAction))
                send(Event.EntityCombatAction(target, targetChosenAction))

                processCombat(source, target, chosenAction, targetChosenAction)
                processCombat(target, source, targetChosenAction, chosenAction)

                send(Event.EntityChanged(source))
                send(Event.EntityChanged(target))

                if (source[living]!!.isDead) send(Event.EntityDied(source))
                if (target[living]!!.isDead) send(Event.EntityDied(target))

                if (target[living]!!.isDead) {
                    //todo finish?
                } else {
                    send(Event.EntityActionEvent(target, EntityInteraction.Combat.actionName))
                }
            }
        }
    }

    private fun processCombat(source: Entity, target: Entity, sourceAction: CombatAction, targetAction: CombatAction) {

        val sourceComponent = source[living]!!
        val targetComponent = target[living]!!

        val damage = getBaseDamage(sourceAction, targetAction)
        val blockedAmount = getBlockedDamage(sourceAction, targetAction, damage)
        val staminaLost = sourceAction.staminaCost

        sourceComponent.stamina -= staminaLost
        targetComponent.health -= damage

        validate(sourceComponent)
        validate(targetComponent)

        send(Event.EntityTakeDamage(target, source, damage, blockedAmount))
    }

    private fun validate(component: LivingComponent) {
        component.stamina = min(component.stamina, component.maxStamina)
        component.health = min(component.health, component.maxHealth)
    }

    private fun getBaseDamage(sourceAction: CombatAction, targetAction: CombatAction) =
        when {
            !sourceAction.hitMade -> 0
            !targetAction.hitTaken -> 0
            else -> (sourceAction.damageMod + targetAction.damageTakenMod)
                .run { if (this < 0) 0 else this }
        }

    private fun getBlockedDamage(sourceAction: CombatAction, targetAction: CombatAction, damage: Int): Int {
        val resultDiff = sourceAction.damageMod - damage
        return when {
            !sourceAction.hitMade -> 0
            !targetAction.hitTaken -> 0
            resultDiff > 0 -> resultDiff
            else -> 0
        }
    }
}
