package me.broot.typedmap.core.api

import kotlin.reflect.KType

/**
 * Key used to access the data in [TypedMap].
 *
 * It is parameterized with the type of a value associated with the key. For example, `ITypedKey<String>` could be
 * used to store/retrieve a `String` instance in the `TypedMap`.
 *
 * In most cases this interface should not be implemented directly. Use
 * [typedKey()][me.broot.typedmap.core.impl.typedKey] or subtype [TypedKey][me.broot.typedmap.core.impl.TypedKey]
 * class in order to create an `ITypedKey`.
 *
 * @see TypedMap
 * @see me.broot.typedmap.core.impl.TypedKey
 * @see me.broot.typedmap.core.impl.typedKey
 */
interface ITypedKey<V> {
//    val keyType: KType

    /**
     * `KType` of the value associated with this key.
     *
     * It must always be the same type as `V`. For example, `valueType` of `ITypedKey<String>` must be a `String` type.
     */
    val valueType: KType
}
