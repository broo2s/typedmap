package me.broot.typedmap.core.impl

import me.broot.typedmap.core.api.ITypedKey
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.typeOf

/**
 * Creates an [ITypedKey] for the provided value type.
 *
 * It allows to easily create unnamed keys for storing and retrieving items by their type. For example, we can store
 * a `User` object and retrieve it later by just looking for the `User` type. We can do this by:
 *
 * ```kotlin
 * val key = typedKey<User>()
 * map[key] = user1
 * ...
 * val user2 = map[key]
 * ```
 *
 * To make it easier to use, extension functions with reified type parameters were provided for the most of typed map
 * operations. Above example could be simplified to:
 *
 * ```kotlin
 * map += user1 // Or: map.setByType(user1)
 * ...
 * val user2 = map.get<User>()
 * ```
 *
 * Accessing items by their type is simple and easy, but it is very limited, e.g. it allows to store only one item
 * per type. More advanced use cases could be implemented with [TypedKey].
 *
 * @see TypedKey
 */
@ExperimentalStdlibApi
public inline fun <reified V> typedKey(): ITypedKey<V> = KeyForType(typeOf<V>())

@PublishedApi
internal class KeyForType<V>(override val valueType: KType) : ITypedKey<V> {
//    override val keyType = KeyForType::class.createType(listOf(KTypeProjection.invariant(valueType)))

    override fun equals(other: Any?): Boolean {
        return this === other || (other is KeyForType<*> && valueType == other.valueType)
    }

    override fun hashCode(): Int {
        return valueType.hashCode()
    }

    override fun toString(): String {
        return "KeyForType($valueType)"
    }
}
