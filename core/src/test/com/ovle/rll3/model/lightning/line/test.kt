package com.ovle.rll3.model.lightning.line

import com.ovle.rll3.model.util.discretization.bresenham.line
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class Test {

    companion object {
        @JvmStatic
        fun args() = testCases
    }

    @ParameterizedTest
    @MethodSource("args")
    fun `test bresenham line`(testCase: TestCase) {
        val (from, to, expectedResult) = testCase
        val line = line(from, to).toTypedArray()
        Assertions.assertArrayEquals(expectedResult, line)
    }
}