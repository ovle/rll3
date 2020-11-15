package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.module.core.entity.locationInfo
import com.ovle.rll3.model.module.core.entity.on
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.task.EntityConditions
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
        target = { position, level -> entityTarget(position, level) { isSourceEntity(it) } },
        turns = 1,
        skillEffect = gatherEffect,
        skillSuccess = { _, t, _ -> !isExists(t.asEntityTarget().entity) }
    ),
    SkillTemplate(
        name = "mine",
        cost = { staminaCost(it, 1) },
        target = { position, level -> isSource(position, level) },
        turns = 1,
        skillEffect = mineEffect,
        skillSuccess = { _, t, l -> isPassable(t.asPositionTarget().position, l) }
    ),
    SkillTemplate(
        name = "attack",
        cost = { staminaCost(it, 1) },
        target = { position, level -> entityTarget(position, level) { isLivingEntity(it) } },
        turns = 1,
        skillEffect = damageEffect,
        skillSuccess = { _, t, _ -> isDead(t.asEntityTarget().entity) }
    ),
    SkillTemplate(
        name = "mass-heal",
        cost = { staminaCost(it, 1) },
        turns = 1,
        skillEffect = healEffect
    )
)

private fun entityTarget(position: GridPoint2, location: LocationInfo, check: (Entity) -> Boolean)
    = location.entities.on(position).singleOrNull { check(it) }
