package me.broot.typedmap.core.util

import me.broot.typedmap.core.test_utils.assertType
import me.broot.typedmap.core.test_utils.compileType
import kotlin.test.*

class OptionalValueTest {
    @Test
    fun `of() should always return present optional`() {
        assertTrue { OptionalValue.of(5).isPresent }
        assertFalse { OptionalValue.of(5).isAbsent }
        assertTrue { OptionalValue.of(null).isPresent }
        assertFalse { OptionalValue.of(null).isAbsent }
    }

    @Test
    fun `ofNullable() should return present or absent optional`() {
        assertTrue { OptionalValue.ofNullable(5).isPresent }
        assertFalse { OptionalValue.ofNullable(5).isAbsent }
        assertFalse { OptionalValue.ofNullable(null).isPresent }
        assertTrue { OptionalValue.ofNullable(null).isAbsent }
    }

    @Test
    fun `absent() should always return absent optional`() {
        assertFalse { OptionalValue.absent().isPresent }
        assertTrue { OptionalValue.absent().isAbsent }
    }

    @Test
    fun `get() on present should return optional value`() {
        assertEquals(5, OptionalValue.of(5).get())
        assertNull(OptionalValue.of(null).get())
        assertEquals(5, OptionalValue.ofNullable(5).get())
    }

    @Test
    @Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")
    fun `get() on absent should throw NoSuchElementException`() {
        assertFailsWith<NoSuchElementException> {
            OptionalValue.absent().get()
        }
        assertFailsWith<NoSuchElementException> {
            OptionalValue.ofNullable(null).get()
        }
    }

    @Test
    fun `getOrNull() on present should return optional value`() {
        assertEquals(5, OptionalValue.of(5).getOrNull())
        assertNull(OptionalValue.of(null).getOrNull())
        assertEquals(5, OptionalValue.ofNullable(5).getOrNull())
    }

    @Test
    fun `getOrNull() on absent should return null`() {
        assertNull(OptionalValue.absent().getOrNull())
        assertNull(OptionalValue.ofNullable(null).getOrNull())
    }

    @Test
    fun `getOrElse() on present should return optional value`() {
        assertEquals(5, OptionalValue.of(5).getOrElse { 7 })
        assertNull(OptionalValue.of(null).getOrElse { 7 })
        assertEquals(5, OptionalValue.ofNullable(5).getOrElse { 7 })
    }

    @Test
    fun `getOrElse() on absent should return else value`() {
        assertEquals(7, OptionalValue.absent().getOrElse { 7 })
        assertEquals(7, OptionalValue.ofNullable(null).getOrElse { 7 })
    }

    @Test
    fun `getOrElse() should pass exceptions to caller`() {
        assertFailsWith<IllegalStateException> {
            OptionalValue.absent().getOrElse { throw IllegalStateException() }
        }
    }

    @Test
    fun `mapValue() on present should return new optional`() {
        assertEquals(10, OptionalValue.of(5).mapValue { it * 2 }.get())
    }

    @Test
    fun `mapValue() on absent should return absent`() {
        assertTrue(OptionalValue.ofNullable<Int>(null).mapValue { it * 2 }.isAbsent)
    }

    @Test
    fun `mapValue() should pass exceptions to caller`() {
        assertFailsWith<IllegalStateException> {
            OptionalValue.of(5).mapValue { throw IllegalStateException() }
        }
    }

    @Test
    fun `Map-getOptional() should return present for existing non-null values`() {
        val map = mapOf("foo" to 5, "bar" to null)
        val value = map.getOptional("foo")
        assertTrue(value.isPresent)
        assertEquals(5, value.get())
    }

    @Test
    fun `Map-getOptional() should return present for existing null values`() {
        val map = mapOf("foo" to 5, "bar" to null)
        val value = map.getOptional("bar")
        assertTrue(value.isPresent)
        assertNull(value.get())

        val map2 = map.toMutableMap()
        map2["baz"] = null
        assertTrue(map2.getOptional("baz").isPresent)
    }

    @Test
    fun `Map-getOptional() should return absent for missing values`() {
        val map = mapOf("foo" to 5, "bar" to null)
        assertTrue(map.getOptional("baz").isAbsent)

        val map2 = map.toMutableMap()
        assertFalse(map2.getOptional("foo").isAbsent)
        map2.remove("foo")
        assertTrue(map2.getOptional("foo").isAbsent)
    }

    @Test
    fun `getOrElse() API tests`() {
        val int = 5
        val number: Number = 5
        val string = "foo"
        val intNullable: Int? = 5

        assertType<Int>(compileType { OptionalValue.of(int).getOrElse { int } })
        assertType<Number>(compileType { OptionalValue.of(int).getOrElse { number } })
        assertType<Number>(compileType { OptionalValue.of(number).getOrElse { int } })
        assertType<Any>(compileType { OptionalValue.of(int).getOrElse { string } })
        assertType<Any>(compileType { OptionalValue.of(string).getOrElse { int } })
        assertType<Int?>(compileType { OptionalValue.of(int).getOrElse { intNullable } })
        assertType<Int?>(compileType { OptionalValue.of(intNullable).getOrElse { int } })

//        Actually, they compile to Any. It may be impossible to fix.
//        assertType<Number>(compileType { OptionalValue.of(int).getOrElse { float } })
//        assertType<Number>(compileType { OptionalValue.of(float).getOrElse { int } })

        assertType<Int>(compileType { OptionalValue.of(int).getOrElse { throw Exception() } })
    }
}
