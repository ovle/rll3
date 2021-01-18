package com.ovle.rll3.model.ai.bt

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.ovle.rll3.BTFactory
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.AIComponent
import com.ovle.rll3.model.module.ai.AISystem
import com.ovle.rll3.model.module.ai.behavior.*
import com.ovle.rll3.model.module.core.component.ComponentMappers.ai
import com.ovle.rll3.model.module.core.entity.entity
import com.ovle.rll3.model.module.core.entity.newGame
import com.ovle.rll3.model.module.core.entity.randomId
import ktx.ashley.get
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class Test {

    companion object {
        private lateinit var ecsEngine: PooledEngine
        private lateinit var aiOwner: Entity
        private lateinit var aiSystem: EntitySystem

        @JvmStatic
        fun args() = testCases

        @JvmStatic
        @BeforeAll
        fun setupEnv() {
            ecsEngine = PooledEngine()
            aiSystem = AISystem(isRealTime = true)
            ecsEngine.addSystem(aiSystem)

            //todo add all other test entities to li
            aiOwner = ecsEngine.entity(randomId(), AIComponent("base"))
            locationInfo.entities.add(aiOwner)

            newGame(locationInfo, worldInfo, ecsEngine)
        }
    }


    @ParameterizedTest
    @MethodSource("args")
    fun `test behavior tree`(testCase: TestCase) {
        val (description, bt, expectedResult) = testCase
        val actualResult: MutableCollection<StepResult> = mutableListOf()

        initBehaviorTree(bt, actualResult)
        runBehaviorTree()

        Assertions.assertArrayEquals(expectedResult, actualResult.toTypedArray(), description)
    }

    private fun initBehaviorTree(bt: BTFactory, actualResult: MutableCollection<StepResult>) {
        val target = TaskTargetHolder()  //todo take from test data
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
        while (isRunning) {
            aiSystem.update(0.1f)   //todo check
        }
    }
}