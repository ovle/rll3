package com.ovle.rll3.model.ecs.component.dto

import com.ovle.rll3.PlayerId
import java.io.Serializable

data class PlayerInfo(
    val playerId: PlayerId
): Serializable