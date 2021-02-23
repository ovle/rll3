package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.util.on
import com.ovle.rll3.model.util.resources
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.util.conditions.EntityConditions.isDead
import com.ovle.rll3.model.util.conditions.EntityConditions.isLivingEntity
import com.ovle.rll3.model.util.conditions.EntityConditions.isSourceEntity
import com.ovle.rll3.model.util.conditions.TileConditions.isMinable
import com.ovle.rll3.model.util.conditions.TileConditions.isPassable


fun skillTemplates() = arrayOf(
    SkillTemplate(
        name = "gather",
        cost = { staminaCost(it, 1) },
        target = { p, l -> entityTarget(p, l) { isSourceEntity(it) } },
        turns = 2,
        effect = gatherEffect,
        isSuccess = { _, t, l -> l.entities.on(t.position()).resources().isNotEmpty() }
    ),
    SkillTemplate(
        name = "mine",
        cost = { staminaCost(it, 1) },
        target = { p, l -> isMinable(p, l) },
        turns = 4,
        effect = mineEffect,
        isSuccess = { _, t, l -> !isMinable(t.asPosition(), l) }
    ),
    SkillTemplate(
        name = "build",
        cost = { staminaCost(it, 1) },
        target = { p, l -> isPassable(p, l) },
        turns = 3,
        effect = buildEffect,
        isSuccess = { _, t, l -> !isPassable(t.asPosition(), l) }    //todo
    ),
    SkillTemplate(
        name = "attack",
        cost = { staminaCost(it, 1) },
        target = { p, l -> entityTarget(p, l) { isLivingEntity(it) } },
        turns = 1,
        effect = damageEffect,
        isSuccess = { _, t, _ -> isDead(t.asEntity()) }
    )
)

private fun entityTarget(position: GridPoint2, location: LocationInfo, check: (Entity) -> Boolean)
    = location.entities.on(position).singleOrNull { check(it) }
