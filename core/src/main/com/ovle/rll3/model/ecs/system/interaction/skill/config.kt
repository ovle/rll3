package com.ovle.rll3.model.ecs.system.interaction.skill

import com.ovle.rll3.model.ecs.component.util.Mappers.living
import ktx.ashley.has

//todo scaling

fun testSkillTemplates() = arrayOf(
    SkillTemplate(
        name = "attack",
        cost = { staminaCost(it, 1) },
        target = { position, level -> entityTarget(position, level) { it.has(living) } },
        time = 50,
        skillEffect = damageEffect
    ),
    SkillTemplate(
        name = "mass-heal",
        cost = { staminaCost(it, 1) },
        time = 100,
        skillEffect = healEffect
    ),
    SkillTemplate(
        name = "jump",
        cost = { staminaCost(it, 1) },
        target = { position, level -> emptyTileTarget(position, level) },
        time = 100,
        skillEffect = jumpToEffect
    )
)