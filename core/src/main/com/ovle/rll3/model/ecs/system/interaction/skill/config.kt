package com.ovle.rll3.model.ecs.system.interaction.skill

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.util.Mappers
import ktx.ashley.get

//sample attack, target: entity
val skill1: Skill = { _, target ->
    target as Entity
    target[Mappers.living]?.let { it.health -= 1 }
}

//sample healing, no target
val skill2: Skill = { source, _ ->
    source[Mappers.living]?.let { it.health += 1 }
}

//sample moving, target: position
val skill3: Skill = { source, target ->
    target as GridPoint2
    val positionComponent = source[Mappers.position]!!
    positionComponent.gridPosition = target
}

fun testSkillTemplates() = arrayOf(
    SkillTemplate(
        name = "attack",
        cost = { staminaCost(it, 1) },
        time = 50,
        skill = skill1
    ),
    SkillTemplate(
        name = "heal-self",
        cost = { staminaCost(it, 1) },
        time = 100,
        skill = skill2
    ),
    SkillTemplate(
        name = "jump",
        cost = { staminaCost(it, 1) },
        time = 100,
        skill = skill3
    )
)