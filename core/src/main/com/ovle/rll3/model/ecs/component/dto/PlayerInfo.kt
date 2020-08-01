package com.ovle.rll3.model.ecs.component.dto

import java.io.Serializable

data class PlayerInfo(
    val playerId: String,
    val templateName: String
): Serializable