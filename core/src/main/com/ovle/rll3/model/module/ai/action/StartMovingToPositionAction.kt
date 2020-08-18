package com.ovle.rll3.model.module.ai.action

import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.BaseTask

// check path first
// no path found case
class StartMovingToPositionAction: BaseTask() {

    override fun executeIntr(): Status {
        EventBus.send(Event.GameEvent.EntityStartMoveCommand(owner, targetPosition))

        return Status.SUCCEEDED
    }
}