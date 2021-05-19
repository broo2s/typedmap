package me.broot.typedmap.core.impl

import me.broot.typedmap.core.test_utils.assertType
import kotlin.test.*

class TypedKeyTest {
    @Test
    fun `valueType should be correct for simple types`() {
        assertType<String>(StringKey.valueType)
        assertType<String>(StringKey2.valueType)
        assertType<Int>(IntKey.valueType)
        assertType<Number>(NumberKey.valueType)
    }

    @Test
    fun `valueType should be correct for parameterized types`() {
        assertType<List<String>>(StringListKey.valueType)
        assertType<List<Int>>(IntListKey.valueType)
        assertType<List<Number>>(NumberListKey.valueType)
        assertType<Collection<Int>>(IntCollectionKey.valueType)
    }

    @Test
    fun `valueType should be correct for nullable types`() {
        assertType<String?>(NullableStringKey.valueType)
        assertType<List<String?>>(NullableStringListKey.valueType)
        assertType<List<String>?>(StringNullableListKey.valueType)
    }

    @Test
    fun `valueType should be correct for subtyped keys`() {
        assertType<String>(StringKey3.valueType)
        assertType<String>(StringKey4.valueType)
    }

    @Test
    fun `distinct keys with the same or similar valueType should not be equal`() {
        assertFalse(StringKey.equals(StringKey2))
        assertFalse(StringKey3.equals(StringKey4))
        assertFalse(StringListKey.equals(IntListKey))
        assertFalse(StringKey.equals(NullableStringKey))
    }

    @Test
    fun `creating TypedKey with erased V should fail`() {
        assertFails {
            ParamKey<String>()
        }
    }

    private object StringKey : TypedKey<String>()
    private object StringKey2 : TypedKey<String>()
    private object IntKey : TypedKey<Int>()
    private object NumberKey : TypedKey<Number>()

    private object StringListKey : TypedKey<List<String>>()
    private object IntListKey : TypedKey<List<Int>>()
    private object NumberListKey : TypedKey<List<Number>>()
    private object IntCollectionKey : TypedKey<Collection<Int>>()

    private object NullableStringKey : TypedKey<String?>()
    private object NullableStringListKey : TypedKey<List<String?>>()
    private object StringNullableListKey : TypedKey<List<String>?>()

    private abstract class AbstractStringKey : TypedKey<String>()
    private object StringKey3 : AbstractStringKey()
    private open class ParamKey<T> : TypedKey<T>()
    private object StringKey4 : ParamKey<String>()
}
