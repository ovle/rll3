package com.ovle.rll3.model.ecs.component.dto

import java.io.Serializable

class TimeInfo: Serializable {
    var turn: Long = 0
    var fractionTicks: Int = 0
}