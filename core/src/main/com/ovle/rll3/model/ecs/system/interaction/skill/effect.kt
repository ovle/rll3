package com.ovle.rll3.model.ecs.system.interaction.skill

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.SkillEffect
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.util.Mappers
import ktx.ashley.get

//todo combine

val damageEffect: SkillEffect = { source, target, amount ->
    target as Entity
    EventBus.send(Event.EntityTakeDamage(target, source, amount))
}

val healEffect: SkillEffect = { source, _, amount ->
    //todo
}

val jumpToEffect: SkillEffect = { source, target, _ ->
    //todo
//    target as GridPoint2
//    val positionComponent = source[Mappers.position]!!
//    positionComponent.gridPosition = target
}