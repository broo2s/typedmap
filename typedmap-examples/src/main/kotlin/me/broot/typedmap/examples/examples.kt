@file:OptIn(ExperimentalStdlibApi::class)
package me.broot.typedmap.examples

import me.broot.typedmap.core.impl.TypedKey
import me.broot.typedmap.core.impl.simpleTypedMap
import me.broot.typedmap.core.util.*

fun main() {
    val sess = simpleTypedMap()

    sess += User("alice")

    println("User: ${sess.get<User>()}")

    fun processUser(user: User) {
        println("Processed user: $user")
    }
    processUser(sess.get())

    fun getUser(): User {
       return sess.get()
    }

    sess += listOf(1, 2, 3, 4, 5)
    sess += listOf("a", "b", "c", "d", "e")

    println("List<Int>: ${sess.get<List<Int>>()}")
    println("List<String>: ${sess.get<List<String>>()}")

    sess[Username] = "alice"
    sess[SessionId] = "0123456789abcdef"
    sess[VisitsCount] = 42

    println("Username: ${sess[Username]}")
    println("SessionId: ${sess[SessionId]}")
    println("Visits: ${sess[VisitsCount]}")

    sess[UserIds] = listOf(1, 2, 3, 4, 5)
    sess[Labels] = listOf("a", "b", "c", "d", "e")

    println("UserIds: ${sess[UserIds]}")
    println("Labels: ${sess[Labels]}")

    sess[OrderKey(1)] = Order(1, listOf("item1", "item2"))
    sess[OrderKey(2)] = Order(2, listOf("item3", "item4"))

    println("OrderKey(1): ${sess[OrderKey(1)]}")
    println("OrderKey(2): ${sess[OrderKey(2)]}")

    sess += AutoOrder(1, listOf("item1", "item2"))
    sess += AutoOrder(2, listOf("item3", "item4"))

    println("AutoOrderKey(1): ${sess[AutoOrderKey(1)]}")
    println("AutoOrderKey(2): ${sess[AutoOrderKey(2)]}")
}

data class User(
    val username: String
)

object Username : TypedKey<String>()
object SessionId : TypedKey<String>()
object VisitsCount : TypedKey<Int>()

object UserIds : TypedKey<List<Int>>()
object Labels : TypedKey<List<String>>()

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
