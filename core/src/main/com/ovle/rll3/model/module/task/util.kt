package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.ovle.rlUtil.gdx.math.adjHV
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.space.position
import com.ovle.rll3.model.util.conditions.EntityConditions.isTaskPerformer
import com.ovle.rll3.model.util.path

//todo finding path on each update is disaster
fun defaultPerformerFilter(e: Entity, t: TaskTarget, l: LocationInfo) =
    isTaskPerformer(e) && havePathToAdjPosition(e, t, l)

private fun havePathToAdjPosition(e: Entity, t: TaskTarget, l: LocationInfo): Boolean {
    val from = e.position()
    val to = t.position()

    return to.adjHV().any { path(from, it, l).isNotEmpty() }
}