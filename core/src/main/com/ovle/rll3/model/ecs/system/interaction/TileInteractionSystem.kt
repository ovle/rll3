package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.special.SelectionMode
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.model.ecs.system.EventSystem


class TileInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<Event.Click> { onClickEvent(it.button, it.point) }

//        EventBus.subscribe<EntityInteractionEvent> { onEntityActionEvent(it.entity, it.interaction) }
//        EventBus.subscribe<EntityUseSkill> { onEntityUseSkillEvent(it.entity, it.target, it.skillTemplate) }
    }

    private fun onClickEvent(button: Int, point: GridPoint2) {
        val interactionInfo = playerInteractionInfo()!!
        if (interactionInfo.selectionMode != SelectionMode.Tile) return

        val selectedTiles = interactionInfo.selectedTiles
        if (point in selectedTiles) {
            interactionInfo.selectedTiles -= point
        } else {
            interactionInfo.selectedTiles += point
        }
    }

//    private fun onEntityActionEvent(entity: Entity, interaction: EntityInteraction) {
//        performEntityInteraction(entity, interaction)
//    }
//
//    //todo
//    private fun onEntityUseSkillEvent(entity: Entity, target: Any?, skillTemplate: SkillTemplate) {
//        println("$entity use skill ${skillTemplate.name} on $target")
////        skill(entity, target, skillTemplate)
//    }
}
