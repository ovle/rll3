package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.SkillEffect
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import ktx.ashley.get

//todo combine

val startMoveEffect: SkillEffect = { source, target, amount ->
    target as GridPoint2
    println("move effect")

    //todo
    source[position]!!.gridPosition = target
//    EventBus.send(EntityStartMoveCommand(source, target))
}

val damageEffect: SkillEffect = { source, target, amount ->
    target as Entity
    println("damage effect")
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