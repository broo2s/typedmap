package me.broot.typedmap.core.impl

import me.broot.typedmap.core.api.ITypedKey
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.full.*

/**
 * Utility class for creating [ITypedKey] instances by subtyping it.
 *
 * It allows to easily create keys for [me.broot.typedmap.core.api.TypedMap]. To create a key, we need to subtype
 * `TypedKey` and provide the associated value type in its `V` type parameter. If we need to store one item per key,
 * we can use singleton keys:
 *
 * ```kotlin
 * object Username : TypedKey<String>()
 * object VisitsCount : TypedKey<Int>()
 *
 * map[Username] = "alice"
 * val visits = map[VisitsCount]
 * ```
 *
 * Sometimes, we need to store multiple items per a key type, for example store multiple users and identify them by
 * their ids. In that case we need to extend [ITypedKey] with additional properties and provide a proper `equals()`
 * and `hashCode()` implementation. The easiest is to use Kotlin's data class:
 *
 * ```kotlin
 * data class UserKey(val id: Int) : TypedKey<User>()
 *
 * map[UserKey(1)] = user1
 * user2 = map[UserKey(2)]
 * ```
 *
 * In fact, this is very similar to regular maps and keys, but still it provides us with its type-safety feature.
 *
 * It is possible to use multi-level subtyping and generics with `TypedKey`, but `V` has to be known at runtime,
 * it can't become removed by type erasure. For example:
 *
 * ```kotlin
 * // This is ok
 * abstract class AbstractStringKey : TypedKey<String>()
 * object SomeStringKey : AbstractStringKey()
 *
 * // This is ok
 * open class ParamKey<T> : TypedKey<T>()
 * object AnotherStringKey : ParamKey<String>()
 *
 * // It throws exception as `String` is erased at compile time.
 * val key = ParamKey<String>()
 * ```
 *
 * @see ITypedKey
 */
abstract class TypedKey<V> : ITypedKey<V> {
//    final override val keyType: KType
    final override val valueType: KType

    init {
        val c = this::class
//        keyType = c.starProjectedType

        // Look for base `TypedKey` KType.
        val baseType = c.allSupertypes.single { it.classifier == TypedKey::class }

        // We assume it is impossible to use star projection or variance when extending `TypedKey`, so `type` property
        // should be always not null and projection should be invariant.
        valueType = checkNotNull(baseType.arguments[0].type)

        require(valueType.classifier !is KTypeParameter) {
            "V type parameter of TypedKey<V> must be known at runtime, it can't become removed by type erasure. See documentation of TypedKey for more details."
        }
    }
}
