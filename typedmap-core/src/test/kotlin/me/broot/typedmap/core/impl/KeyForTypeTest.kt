package me.broot.typedmap.core.impl

import me.broot.typedmap.core.test_utils.assertType
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalStdlibApi::class)
class KeyForTypeTest {
    @Test
    fun `valueType should be correct`() {
        assertType<String>(typedKey<String>().valueType)
        assertType<String?>(typedKey<String?>().valueType)
        assertType<List<String>>(typedKey<List<String>>().valueType)
    }

    @Test
    fun `keys with the same type should be equal`() {
        assertTrue(typedKey<String>() == typedKey<String>())
        assertTrue(typedKey<String?>() == typedKey<String?>())
        assertTrue(typedKey<List<String>>() == typedKey<List<String>>())
    }

    @Test
    fun `keys with different types should not be equal`() {
        assertFalse(typedKey<String>() == typedKey<Int>())
        assertFalse(typedKey<String>() == typedKey<String?>())
        assertFalse(typedKey<List<String>>() == typedKey<List<Int>>())
        assertFalse(typedKey<List<String>>() == typedKey<Collection<String>>())
    }
}