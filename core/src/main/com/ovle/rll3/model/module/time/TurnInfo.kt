package com.ovle.rll3.model.module.time

import java.io.Serializable

enum class TurnStatus {
//    Init,
    WaitPlayer,
    WaitAI,
    Apply
//    Cleanup
}

class TurnInfo: Serializable {
    var turn: Long = 0
    var status: TurnStatus = TurnStatus.WaitPlayer
}