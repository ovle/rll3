package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.SkillEffect
import com.ovle.rll3.event.Event.GameEvent.EntityGatheredEvent
import com.ovle.rll3.event.Event.GameEvent.EntityTakeDamageEvent
import com.ovle.rll3.event.EventBus

//todo combine

val damageEffect: SkillEffect = { source, target, amount ->
    target as Entity
    EventBus.send(EntityTakeDamageEvent(target, source, amount))
}

val areaDamageEffect: SkillEffect = { source, target, amount ->
    target as GridPoint2
    //todo
//    EventBus.send(EntityTakeDamageEvent(target, source, amount))
}

val buffEffect: SkillEffect = { source, _, amount ->
    //todo
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