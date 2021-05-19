package me.broot.typedmap.core

import me.broot.typedmap.core.api.MutableTypedMap
import me.broot.typedmap.core.impl.TypedKey
import kotlin.test.*

@OptIn(ExperimentalStdlibApi::class)
abstract class AbstractMutableTypedMapTest<T : MutableTypedMap> {
    protected abstract fun createInstance(): T

    private lateinit var _map: T
    protected val map: T get() = _map

    @BeforeTest
    fun beforeTest() {
        _map = createInstance()
    }

    @Test
    fun `get() for existing item should return it`() {
        map[Username] = "alice"
        assertEquals("alice", map[Username])
    }

    @Test
    fun `get() for missing item should throw NoSuchElementException`() {
        map[Username] = "alice"
        assertFailsWith<NoSuchElementException> {
            map[SessionId]
        }
    }

    @Test
    fun `getOrNull() for existing item should return it`() {
        map[Username] = "alice"
        assertEquals("alice", map.getOrNull(Username))
    }

    @Test
    fun `getOrNull() for missing item should return null`() {
        map[Username] = "alice"
        assertNull(map.getOrNull(SessionId))
    }

    @Test
    fun `contains() for existing item should return true`() {
        map[Username] = "alice"
        assertTrue(Username in map)
    }

    @Test
    fun `contains() for missing item should return false`() {
        map[Username] = "alice"
        assertFalse(SessionId in map)
    }

    @Test
    fun `getOptional() for existing item should return present optional`() {
        map[Username] = "alice"
        val opt = map.getOptional(Username)
        assertTrue(opt.isPresent)
        assertEquals("alice", opt.get())
    }

    @Test
    fun `getOptional() for missing item should return absent optional`() {
        map[Username] = "alice"
        assertTrue(map.getOptional(SessionId).isAbsent)
    }

    @Test
    fun `set() should replace existing item`() {
        map[Username] = "alice"
        map[Username] = "bob"
        assertEquals("bob", map[Username])
    }

    @Test
    fun `remove() for existing item should remove it and return it`() {
        map[Username] = "alice"
        val username = map.remove(Username)
        assertEquals("alice", username)
        assertFalse(Username in map)
    }

    @Test
    fun `remove() for missing item should throw NoSuchElementException`() {
        map[Username] = "alice"
        assertFailsWith<NoSuchElementException> {
            map.remove(SessionId)
        }
    }

    @Test
    fun `removeIfSet() for existing item should remove it and return it`() {
        map[Username] = "alice"
        val username = map.removeIfSet(Username)
        assertEquals("alice", username)
        assertFalse(Username in map)
    }

    @Test
    fun `removeIfSet() for missing item should do nothing and return null`() {
        map[Username] = "alice"
        assertNull(map.removeIfSet(SessionId))
    }

    private object Username : TypedKey<String>()
    private object SessionId : TypedKey<String>()
    private object Visits : TypedKey<Int>()

    private data class User(
        val name: String
    )
}
