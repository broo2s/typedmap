package me.broot.typedmap.core.api

import me.broot.typedmap.core.util.OptionalValue

/**
 * Type-safe heterogeneous map with read-only access.
 *
 * Type-safe heterogeneous map (or typed map) allows to store items of various types and access them in a type-safe
 * manner. This is possible by parameterizing keys used with this map.
 *
 * This interface represents a read-only access to a typed map. Read-write access is provided by [MutableTypedMap].
 *
 * The easiest way to create a `TypedMap` is to use [simpleTypedMap()][me.broot.typedmap.core.impl.simpleTypedMap].
 *
 * @see MutableTypedMap
 * @see ITypedKey
 * @see me.broot.typedmap.core.impl.simpleTypedMap
 */
interface TypedMap {
    /**
     * Returns a value associated with the provided key.
     *
     * It throws `NoSuchElementException` if an item does not exist.
     *
     * @throws NoSuchElementException
     */
    operator fun <V> get(key: ITypedKey<V>): V = getOptional(key).get()

    /**
     * Returns a value associated with the provided key or null if an item does not exist.
     */
    fun <V> getOrNull(key: ITypedKey<V>): V? = getOptional(key).getOrNull()

    /**
     * Checks whether the provided key exists in the map.
     */
    operator fun <V> contains(key: ITypedKey<V>): Boolean = getOptional(key).isPresent

    /**
     * Returns a value associated with the provided key with three possible states: non-null value, null or not exist.
     *
     * This is useful if a value is nullable and we need to distinguish whether it was set explicitly to null or
     * it was not set.
     *
     * @see OptionalValue
     */
    fun <V> getOptional(key: ITypedKey<V>): OptionalValue<V>
}
