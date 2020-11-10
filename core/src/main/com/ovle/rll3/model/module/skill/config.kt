package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.module.core.entity.on
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.task.EntityConditions.isLivingEntity
import com.ovle.rll3.model.module.task.EntityConditions.isSourceEntity
import com.ovle.rll3.model.module.task.TileConditions.isPassable
import com.ovle.rll3.model.module.task.TileConditions.isSource

fun skillTemplates() = arrayOf(
    SkillTemplate(
        name = "gather",
        cost = { staminaCost(it, 1) },
        target = { position, level -> entityTarget(position, level) { isSourceEntity(it) } },
        time = 50,
        skillEffect = gatherEffect
    ),
    SkillTemplate(
        name = "mine",
        cost = { staminaCost(it, 1) },
        target = { position, level -> isSource(position, level) },
        time = 150,
        skillEffect = mineEffect
    ),
    SkillTemplate(
        name = "attack",
        cost = { staminaCost(it, 1) },
        target = { position, level -> entityTarget(position, level) { isLivingEntity(it) } },
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
        target = { position, level -> isPassable(position, level) },
        time = 100,
        skillEffect = jumpToEffect
    )
)

private fun entityTarget(position: GridPoint2, location: LocationInfo, check: (Entity) -> Boolean)
    = location.entities.on(position).singleOrNull { check(it) }
