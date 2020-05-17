package com.ovle.rll3.model.ecs.component.special

import com.badlogic.ashley.core.Component
import com.ovle.rll3.model.ecs.system.level.PlayerId
import java.util.*


data class PlayerInfo(
    val playerId: String,
    val templateName: String
)

class PlayerComponent(
    var player: PlayerInfo
): Component