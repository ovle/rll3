package com.ovle.rll3.model.module.ai.action

import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.BaseTask

class TaskSuccessAction: BaseTask() {

    override fun executeIntr(): Status {
        EventBus.send(Event.GameEvent.TaskSucceedCommand(task))

        return Status.SUCCEEDED
    }
}