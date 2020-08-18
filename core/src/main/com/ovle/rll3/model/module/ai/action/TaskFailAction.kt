package com.ovle.rll3.model.module.ai.action

import com.ovle.rll3.event.Event.GameEvent.TaskFailCommand
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.BaseTask

class TaskFailAction: BaseTask() {

    override fun executeIntr(): Status {
        EventBus.send(TaskFailCommand(task))

        return Status.SUCCEEDED
    }
}