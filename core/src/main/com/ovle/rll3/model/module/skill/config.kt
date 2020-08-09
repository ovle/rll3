package com.ovle.rll3.model.module.skill

import com.ovle.rll3.model.module.core.component.Mappers.living
import com.ovle.rll3.model.module.core.component.Mappers.source
import ktx.ashley.has

fun testSkillTemplates() = arrayOf(
    SkillTemplate(
        name = "gather",
        cost = { staminaCost(it, 1) },
        target = { position, level -> entityTarget(position, level) { it.has(source) } },
        time = 50,
        skillEffect = gatherEffect
    ),
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