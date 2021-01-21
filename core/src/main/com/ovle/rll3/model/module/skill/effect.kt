package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.SkillEffect
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.procedural.config.location.lowGroundTileId
import com.ovle.rll3.model.procedural.config.location.structureWallSTileId

//todo combine
//todo commands, not events

val damageEffect: SkillEffect = { info, amount ->
    val (_, source, target, _) = info
    target as Entity
    send(EntityTakeDamageEvent(target, source, amount))
}

val gatherEffect: SkillEffect = { info, _ ->
    val (_, source, target, _) = info
    target as Entity
    send(EntityGatheredEvent(target, source))
}

val mineEffect: SkillEffect = { info, _ ->
    val (_, _, target, _) = info
    send(ChangeTileCommand(lowGroundTileId, target as GridPoint2)) //todo tileId
}

val buildEffect: SkillEffect = { info, _ ->
    val (_, _, target, payload) = info
    send(ChangeTileCommand(structureWallSTileId, target as GridPoint2)) //todo tileId

    val material = payload as Entity?
    material?.let {
        send(DestroyEntityCommand(it))
    }
}