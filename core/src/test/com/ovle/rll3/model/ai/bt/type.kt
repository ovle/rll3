package com.ovle.rll3.model.ai.bt

import com.ovle.rll3.BTFactory
import com.ovle.rll3.model.module.ai.behavior.TaskExecResult

data class StepResult(
    val step: String,
    val result: TaskExecResult
)

fun step(step: String, result: TaskExecResult) = StepResult(step, result)

data class TestCase(
    val description: String,
    val bt: BTFactory,
    val expectedResult: Array<StepResult>
)