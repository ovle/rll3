package com.ovle.rll3.model.module.skill

import com.ovle.rll3.model.module.core.component.ComponentMappers.health
import com.ovle.rll3.point
import ktx.ashley.has

fun skillTemplates() = arrayOf(
    SkillTemplate(
        name = "move",
        icon = point(5, 0),
        target = { position, level -> emptyTileTarget(position, level) },
        time = 25,
        skillEffect = startMoveEffect
    ),
    SkillTemplate(
        name = "attack",
        icon = point(0, 4),
        cost = { staminaCost(it, 1) },
        target = { position, level -> entityTarget(position, level) { it.has(health) } },
        time = 50,
        skillEffect = damageEffect
    ),
    SkillTemplate(
        name = "fireball",
        icon = point(1, 4),
        cost = { staminaCost(it, 1) },
        target = { position, level -> emptyTileTarget(position, level) },
        time = 100,
        skillEffect = areaDamageEffect
    ),
    SkillTemplate(
        name = "heal-self",
        icon = point(2, 4),
        cost = { staminaCost(it, 1) },
        time = 100,
        skillEffect = healEffect
    ),
    SkillTemplate(
        name = "war-cry",
        icon = point(3, 4),
        cost = { staminaCost(it, 1) },
        time = 100,
        skillEffect = buffEffect
    )
)