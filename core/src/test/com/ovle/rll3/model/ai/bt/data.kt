package com.ovle.rll3.model.ai.bt

import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.Task.Status.*
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.BTFactory
import com.ovle.rll3.model.module.ai.behavior.*
import com.ovle.rll3.model.module.ai.behavior.config.task.failTask
import com.ovle.rll3.model.module.ai.behavior.config.task.successTask
import com.ovle.rll3.model.procedural.config.world.desertTileId
import com.ovle.rll3.point

//todo test all steps?
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


val testCases = arrayOf(
    TestCase(
        description = "seq + select",
        bt = { initialTarget ->
            tree {
                seq {
                    select {
                        task("1.1", failTask())
                        task("1.2", successTask())
                    }
                    task("2", successTask())
                }
            }
        },
        expectedResult = arrayOf(
            step("1.1", TaskExecResult(FAILED)),
            step("1.2", TaskExecResult(SUCCEEDED)),
            step("2", TaskExecResult(SUCCEEDED))
        )
    )
)