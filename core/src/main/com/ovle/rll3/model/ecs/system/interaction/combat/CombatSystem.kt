package com.ovle.rll3.model.ecs.system.interaction.combat

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.advanced.LivingComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.living
import com.ovle.rll3.model.ecs.system.EventSystem
import com.ovle.rll3.model.ecs.system.interaction.EntityInteraction
import com.ovle.rll3.model.ecs.system.interaction.EntityInteractionType
import ktx.ashley.get
import java.lang.Integer.min

@Deprecated("will use skills instead")
class CombatSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<Event.EntityInteractionEvent> { onEntityInteractionEvent(it.source, it.entity, it.interaction) }
    }

    private fun onEntityInteractionEvent(source: Entity, target: Entity, interaction: EntityInteraction) {
        when (interaction.type) {
            EntityInteractionType.Skill -> {
                //todo
//                val chosenAction = combatActions.single { it.name == interaction }
//                processCombat(source, target, chosenAction)

                send(Event.EntityChanged(source))
                send(Event.EntityChanged(target))

//                if (source[living]!!.isDead) send(Event.EntityDied(source))
                if (target[living]!!.isDead) send(Event.EntityDied(target))
            }
        }
    }

    private fun processCombat(source: Entity, target: Entity, sourceAction: CombatAction) {
        val sourceComponent = source[living]!!
        val targetComponent = target[living]!!

        val damage = getBaseDamage(sourceAction)
        val blockedAmount = getBlockedDamage(sourceAction, damage)
        val staminaLost = sourceAction.staminaCost

        sourceComponent.stamina -= staminaLost
        targetComponent.health -= damage

        validate(sourceComponent)
        validate(targetComponent)

        if (damage > 0) {
            send(Event.EntityTakeDamage(target, source, damage, blockedAmount))
        }
    }

    private fun validate(component: LivingComponent) {
        with(component) {
            stamina = min(stamina, maxStamina)
            health = min(health, maxHealth)
        }
    }

    private fun getBaseDamage(sourceAction: CombatAction) =
        when {
            !sourceAction.hitMade -> 0
            else -> (sourceAction.damageMod)
                .run { if (this < 0) 0 else this }
        }

    private fun getBlockedDamage(sourceAction: CombatAction, damage: Int): Int {
        val resultDiff = sourceAction.damageMod - damage
        return when {
            !sourceAction.hitMade -> 0
            resultDiff > 0 -> resultDiff
            else -> 0
        }
    }
}
