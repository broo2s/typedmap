package me.broot.typedmap.core.util

import me.broot.typedmap.core.api.MutableTypedMap
import me.broot.typedmap.core.api.TypedMap
import me.broot.typedmap.core.impl.typedKey

/**
 * Returns a value of the provided type.
 *
 * @throws NoSuchElementException
 * @see TypedMap.get
 * @see typedKey
 */
@ExperimentalStdlibApi
inline fun <reified V> TypedMap.get(): V = get(typedKey())

/**
 * Returns a value of the provided type or null if it does not exist.
 *
 * @see TypedMap.getOrNull
 * @see typedKey
 */
@ExperimentalStdlibApi
inline fun <reified V> TypedMap.getOrNull(): V? = getOrNull(typedKey<V>())

/**
 * Checks whether a value of the provided type exists in the map.
 *
 * @see TypedMap.contains
 * @see typedKey
 */
@ExperimentalStdlibApi
inline fun <reified V> TypedMap.contains(): Boolean = contains(typedKey<V>())

/**
 * Returns a value of the provided type with three possible states: non-null value, null or not exist.
 *
 * @see TypedMap.getOptional
 * @see typedKey
 * @see OptionalValue
 */
@ExperimentalStdlibApi
inline fun <reified V> TypedMap.getOptional(): OptionalValue<V> = getOptional(typedKey())

/**
 * Removes a value of the provided type and returns this value.
 *
 * @throws NoSuchElementException
 * @see MutableTypedMap.remove
 * @see typedKey
 */
@ExperimentalStdlibApi
inline fun <reified V> MutableTypedMap.remove(): V = remove(typedKey())

/**
 * Removes a value of the provided type. Returns this value or null if it does not exist.
 *
 * @see MutableTypedMap.removeIfSet
 * @see typedKey
 */
@ExperimentalStdlibApi
inline fun <reified V> MutableTypedMap.removeIfSet(): V? = removeIfSet(typedKey<V>())

/**
 * Stores the value and identify it by its type.
 *
 * @see MutableTypedMap.set
 * @see typedKey
 */
@ExperimentalStdlibApi
inline fun <reified V> MutableTypedMap.setByType(value: V) {
    set(typedKey(), value)
}

/**
 * Stores the value and identify it by its type.
 *
 * @see setByType
 * @see MutableTypedMap.set
 * @see typedKey
 */
@ExperimentalStdlibApi
inline operator fun <reified V> MutableTypedMap.plusAssign(value: V) = setByType(value)

/**
 * Returns read-only wrapper around `MutableTypedMap`.
 *
 * Returned map reacts to changes in the original map.
 */
fun MutableTypedMap.toTypedMap(): TypedMap = object : TypedMap by this {}
