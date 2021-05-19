package me.broot.typedmap.core.test_utils

import kotlin.test.Test
import kotlin.test.assertEquals

class TestUtilsTest {
    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `compileType() and assertType() tests`() {
        assertType<String>(compileType { "foo" })
        assertType<Int>(compileType { 5 })

        val intAsNumber: Number = 5
        assertEquals(Int::class, intAsNumber::class)
        assertType<Number>(compileType { intAsNumber })

        assertType<Int?>(compileType { "5".toIntOrNull() })
        assertType<List<Int>>(compileType { listOf(1, 2, 3) })
        assertType<Unit>(compileType {})
        assertEquals(NOTHING_TYPE, compileType { throw Exception() })
    }
}

private fun returnsNothing(): Nothing = throw Exception()
private val NOTHING_TYPE = ::returnsNothing.returnType
