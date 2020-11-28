package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.module.core.entity.on
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.task.EntityConditions.isDead
import com.ovle.rll3.model.module.task.EntityConditions.isExists
import com.ovle.rll3.model.module.task.EntityConditions.isLivingEntity
import com.ovle.rll3.model.module.task.EntityConditions.isSourceEntity
import com.ovle.rll3.model.module.task.TileConditions.isPassable
import com.ovle.rll3.model.module.task.TileConditions.isSource
import com.ovle.rll3.model.module.task.asEntityTarget
import com.ovle.rll3.model.module.task.asPositionTarget

fun skillTemplates() = arrayOf(
    SkillTemplate(
        name = "gather",
        cost = { staminaCost(it, 1) },
        target = { p, l -> entityTarget(p, l) { isSourceEntity(it) } },
        turns = 2,
        effect = gatherEffect,
        isSuccess = { _, t, _ -> !isExists(t.asEntityTarget().entity) }
    ),
    SkillTemplate(
        name = "mine",
        cost = { staminaCost(it, 1) },
        target = { p, l -> isSource(p, l) },
        turns = 4,
        effect = mineEffect,
        isSuccess = { _, t, l -> !isSource(t.asPositionTarget().position, l) }
    ),
    SkillTemplate(
        name = "build",
        cost = { staminaCost(it, 1) },
        target = { p, l -> isPassable(p, l) },
        turns = 3,
        effect = buildEffect,
        isSuccess = { _, t, l -> !isPassable(t.asPositionTarget().position, l) }    //todo
    ),
    SkillTemplate(
        name = "attack",
        cost = { staminaCost(it, 1) },
        target = { p, l -> entityTarget(p, l) { isLivingEntity(it) } },
        turns = 1,
        effect = damageEffect,
        isSuccess = { _, t, _ -> isDead(t.asEntityTarget().entity) }
    )
)

private fun entityTarget(position: GridPoint2, location: LocationInfo, check: (Entity) -> Boolean)
    = location.entities.on(position).singleOrNull { check(it) }
