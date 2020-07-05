package com.ovle.rll3.model.ecs.system.interaction.skill

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.util.Mappers
import ktx.ashley.get

//todo combines

val damageEffect: SkillEffect = { _, target ->
    target as Entity
    target[Mappers.living]?.let { it.health -= 1 }
}

val healEffect: SkillEffect = { source, _ ->
    //todo
}

val jumpToEffect: SkillEffect = { source, target ->
    target as GridPoint2
    val positionComponent = source[Mappers.position]!!
    positionComponent.gridPosition = target
}