package com.ovle.rll3.model.ai.bt

import com.badlogic.ashley.core.PooledEngine
import com.ovle.rll3.BTFactory
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.util.event.EventBus
import com.ovle.rll3.model.module.ai.behavior.*
import com.ovle.rll3.model.module.core.component.ComponentMappers.ai
import com.ovle.rll3.model.module.core.entity.entity
import com.ovle.rll3.model.module.core.entity.newGame
import com.ovle.rll3.model.module.task.TaskTarget
import ktx.ashley.get
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

//todo separate bt and game logic tests

class Test {

    companion object {
        private lateinit var ecsEngine: PooledEngine

        @JvmStatic
        fun args() = testCases

        @JvmStatic
        @BeforeAll
        fun setupGlobal() {
            ecsEngine = PooledEngine()
            newGame(locationInfo, worldInfo, ecsEngine)
        }
    }


    @ParameterizedTest
    @MethodSource("args")
    fun `test behavior tree`(testCase: TestCase) {
        val (description, _, bt, initialTarget, expectedResult) = testCase
        val actualResult: MutableCollection<StepResult> = mutableListOf()

        setup(testCase.environment)

        initBehaviorTree(bt, initialTarget, actualResult)
        runBehaviorTree()
        Assertions.assertArrayEquals(expectedResult, actualResult.toTypedArray(), description)
        //todo check game state in separate test?

        cleanup(testCase.environment)
    }

    private fun setup(environment: TestEnvironment) {
        environment.systems.forEach {
            ecsEngine.addSystem(it)
        }
        environment.entities.forEach {
            val e = ecsEngine.entity(it.id, *it.components)

            locationInfo.entities.add(e)
        }
    }

    private fun cleanup(environment: TestEnvironment) {
        environment.systems.forEach {
            ecsEngine.removeSystem(it)
        }
        environment.entities.forEach {
            val e = entity(it.id, ecsEngine.entities.toList())
            ecsEngine.removeEntity(e)
        }

        locationInfo.entities.clear()
    }

    private fun initBehaviorTree(bt: BTFactory, initialTarget: TaskTarget?, actualResult: MutableCollection<StepResult>) {
        val target = TaskTargetHolder(initialTarget)
        val aiOwner = entity("aiOwner", ecsEngine.entities.toList())
        val btParams = BTParams(aiOwner, null, ecsEngine)

        val behaviorTree = bt.invoke(target).apply {
            `object` = btParams
            val statusListener = TestTaskStatusListener(this, actualResult)
            addListener(statusListener)
        }
        aiOwner[ai]!!.behaviorTree = behaviorTree
    }

    private fun runBehaviorTree() {
        var isRunning = true
        EventBus.subscribe<BtFinishedEvent> {
            isRunning = false
        }
        var totalTime = 0.0f
        val maxTime = 5.0f
        while (isRunning && totalTime <= maxTime) {
            val delta = 0.1f
            ecsEngine.update(delta)
            totalTime += delta
        }
    }
}