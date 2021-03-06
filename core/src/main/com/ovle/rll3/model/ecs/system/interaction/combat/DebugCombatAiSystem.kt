package com.ovle.rll3.model.ecs.system.interaction.combat

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event.DebugCombatEvent
import com.ovle.rll3.event.Event.EntityInteractionEvent
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.util.Mappers.action
import com.ovle.rll3.model.ecs.component.util.Mappers.living
import com.ovle.rll3.model.ecs.component.util.Mappers.render
import com.ovle.rll3.model.ecs.entity.entity
import com.ovle.rll3.model.ecs.system.EventSystem
import com.ovle.rll3.model.ecs.system.interaction.EntityInteraction
import ktx.ashley.get

@Deprecated("will use skills instead")
class DebugCombatAiSystem : EventSystem() {

    private var isDebugCombat = false

    private var e1: Entity? = null
    private var e2: Entity? = null

    override fun subscribe() {
        EventBus.subscribe<DebugCombatEvent> { onToggleDebugCombatEvent() }
        EventBus.subscribe<EntityInteractionEvent> { onEntityInteractionEvent(it.source, it.entity, it.interaction) }
    }

    private fun onToggleDebugCombatEvent() {
        isDebugCombat = !isDebugCombat

        e1 = entity("b1")
        e2 = entity("b2")
        e2!![render]!!.flip()
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        if (!isDebugCombat) return
        if (e1 == null || e2 == null) return

        val isDeadE1 = e1!![living]!!.isDead
        val isDeadE2 = e2!![living]!!.isDead
        if (!isDeadE1 && !isDeadE2) {
            checkAction(e1, e2)
            checkAction(e2, e1)
        } else {
            e1 = null
            e2 = null
        }
    }

    private fun checkAction(entity: Entity?, target: Entity?) {
        val actionComponent = entity!![action]!!
        if (actionComponent.current == null) {
            //todo
//            val combatAction = combatActions.find { it.name == "strike" }!!
//            actionComponent.current = { send(EntityInteractionEvent(entity, target!!, combatAction.name)) }
//            actionComponent.animation = AnimationType.Attack
//            actionComponent.timeLeft = combatAction.timeCost
        }
    }

    private fun onEntityInteractionEvent(source: Entity, target: Entity, interaction: EntityInteraction) {

    }
}
