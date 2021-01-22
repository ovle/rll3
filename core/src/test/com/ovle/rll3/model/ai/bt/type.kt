package com.ovle.rll3.model.ai.bt

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.BTFactory
import com.ovle.rll3.EntityId
import com.ovle.rll3.model.module.ai.behavior.TaskExecResult
import com.ovle.rll3.model.module.core.component.CoreComponent
import com.ovle.rll3.model.module.task.TaskTarget

data class StepResult(
    val step: String,
    val result: TaskExecResult
)

fun step(step: String, result: TaskExecResult) = StepResult(step, result)

data class TestCase(
    val description: String,
    val environment: TestEnvironment,
    val bt: BTFactory,
    val initialTarget: TaskTarget? = null,
    val expectedResult: Array<StepResult>
)

data class TestEnvironment(
    val systems: Array<EntitySystem>,
    val entities: Array<EntityInfo>
)

data class EntityInfo(
    val id: EntityId,
    val components: Array<Component>
)

fun ent(id: EntityId, vararg components: Component) =
    EntityInfo(id, (components as Array<Component>) + CoreComponent(id = id))