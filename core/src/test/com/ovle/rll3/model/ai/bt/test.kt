package com.ovle.rll3.model.ai.bt

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.AIComponent
import com.ovle.rll3.model.module.ai.AISystem
import com.ovle.rll3.model.module.ai.behavior.TaskTargetHolder
import com.ovle.rll3.model.module.core.component.ComponentMappers.ai
import com.ovle.rll3.model.module.core.entity.entity
import com.ovle.rll3.model.module.core.entity.randomId
import ktx.ashley.get
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class Test {

    companion object {
        private lateinit var ecsEngine: PooledEngine
        private lateinit var e: Entity
        private lateinit var aiSystem: EntitySystem

        @JvmStatic
        fun args() = testCases

        @JvmStatic
        @BeforeAll
        fun setupEnv() {
            ecsEngine = PooledEngine()
            e = ecsEngine.entity(randomId(), AIComponent("base"))
            aiSystem = AISystem(isRealTime = true)
            //todo init locationInfo
            ecsEngine.addSystem(aiSystem)
        }
    }



    @ParameterizedTest
    @MethodSource("args")
    fun `test behavior tree`(testCase: TestCase) {
        val (description, bt, expectedResult) = testCase

        val target = TaskTargetHolder()  //todo take from test data
        e[ai]!!.behaviorTree = bt.invoke(target)

        var isRunning = true
        EventBus.subscribe<BtFinishedEvent> {
            isRunning = false
        }

        while (isRunning) {
            aiSystem.update(0.1f)
        }

        val actualResult: Array<StepResult> = arrayOf() //todo get from the system somehow

        Assertions.assertArrayEquals(expectedResult, actualResult, description)
    }
}