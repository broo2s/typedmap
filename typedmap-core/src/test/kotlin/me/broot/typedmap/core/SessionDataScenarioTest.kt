package me.broot.typedmap.core

import me.broot.typedmap.core.impl.TypedKey
import me.broot.typedmap.core.impl.simpleTypedMap
import me.broot.typedmap.core.util.*
import kotlin.test.*

@OptIn(ExperimentalStdlibApi::class)
class SessionDataScenarioTest {
    @Test
    fun test() {
        val sess = simpleTypedMap()

        sess += User("alice")
        assertEquals("alice", sess.get<User>().username)

        sess += User("bob")
        assertEquals("bob", sess.get<User>().username)

        assertTrue(sess.contains<User>())
        sess.remove<User>()
        assertFalse(sess.contains<User>())
        assertFailsWith<NoSuchElementException> {
            sess.get<User>()
        }

        sess += listOf(1, 2, 3, 4, 5)
        sess += listOf("a", "b", "c", "d", "e")
        assertEquals(listOf(1, 2, 3, 4, 5), sess.get())
        assertEquals(listOf("a", "b", "c", "d", "e"), sess.get())

        sess[Username] = "alice"
        sess[SessionId] = "0123456789abcdef"
        sess[VisitsCount] = 42
        assertEquals("alice", sess[Username])
        assertEquals("0123456789abcdef", sess[SessionId])
        assertEquals(42, sess[VisitsCount])

        sess[Username] = "bob"
        assertEquals("bob", sess[Username])

        assertTrue(Username in sess)
        sess.remove(Username)
        assertFalse(Username in sess)
        assertFailsWith<NoSuchElementException> {
            sess[Username]
        }

        sess[UserIds] = listOf(1, 2, 3, 4, 5)
        sess[Labels] = listOf("a", "b", "c", "d", "e")
        assertEquals(listOf(1, 2, 3, 4, 5), sess[UserIds])
        assertEquals(listOf("a", "b", "c", "d", "e"), sess[Labels])

        val o1 = Order(1, listOf("item1", "item2"))
        val o2 = Order(2, listOf("item3", "item4"))
        val o3 = Order(3, listOf("item5", "item6"))
        sess[OrderKey(1)] = o1
        sess[OrderKey(2)] = o2
        sess += o3

        assertEquals(o1, sess[OrderKey(1)])
        assertEquals(o2, sess[OrderKey(2)])
        assertFails { sess[OrderKey(3)] }

        assertEquals(o3, sess.get())
        sess += o2
        assertEquals(o2, sess.get())

        val ao1 = AutoOrder(1, listOf("item1", "item2"))
        val ao2 = AutoOrder(2, listOf("item3", "item4"))
        val ao3 = AutoOrder(3, listOf("item5", "item6"))
        sess += ao1
        sess += ao2
        sess += ao3

        assertEquals(ao1, sess[AutoOrderKey(1)])
        assertEquals(ao2, sess[AutoOrderKey(2)])
        assertEquals(ao3, sess[AutoOrderKey(3)])

        assertFails { sess.get<AutoOrder>() }
        sess.setByType(ao1)
        assertEquals(ao1, sess.get())
    }

    private object Username : TypedKey<String>()
    private object SessionId : TypedKey<String>()
    private object VisitsCount : TypedKey<Int>()

    object UserIds : TypedKey<List<Int>>()
    object Labels : TypedKey<List<String>>()

    data class User(
        val username: String
    )

    data class Order(
        val orderId: Int,
        val items: List<String>
    )

    data class OrderKey(
        val orderId: Int
    ) : TypedKey<Order>()

    data class AutoOrder(
        val orderId: Int,
        val items: List<String>
    ) : AutoKey<AutoOrder> {
        override val typedKey get() = AutoOrderKey(orderId)
    }

    data class AutoOrderKey(
        val orderId: Int
    ) : TypedKey<AutoOrder>()
}