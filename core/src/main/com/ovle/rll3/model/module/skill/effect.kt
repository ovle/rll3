package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.SkillEffect
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.procedural.config.location.lowGroundTileId
import com.ovle.rll3.model.procedural.config.location.structureWallSTileId

//todo combine
//todo commands, not events
val damageEffect: SkillEffect = { source, target, amount ->
    target as Entity
    EventBus.send(EntityTakeDamageEvent(target, source, amount))
}

val gatherEffect: SkillEffect = { source, target, _ ->
    target as Entity
    EventBus.send(EntityGatheredEvent(target, source))
}

val mineEffect: SkillEffect = { source, target, amount ->
    EventBus.send(ChangeTileCommand(lowGroundTileId, target as GridPoint2)) //todo tileId
}

val buildEffect: SkillEffect = { source, target, amount ->
    EventBus.send(ChangeTileCommand(structureWallSTileId, target as GridPoint2)) //todo tileId
}