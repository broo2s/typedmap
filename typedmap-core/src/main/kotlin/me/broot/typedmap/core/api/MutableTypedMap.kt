package me.broot.typedmap.core.api

/**
 * [TypedMap] with read-write access.
 *
 * It extends `TypedMap` with methods used to modify the contents of the map.
 *
 * @see TypedMap
 * @see ITypedKey
 * @see me.broot.typedmap.core.impl.simpleTypedMap
 */
interface MutableTypedMap : TypedMap {
    /**
     * Stores the value and associates it with the provided key.
     */
    operator fun <V> set(key: ITypedKey<V>, value: V)

    /**
     * Removes a value associated with the provided key and returns this value.
     *
     * It throws `NoSuchElementException` if an item does not exist.
     *
     * @throws NoSuchElementException
     */
    fun <V> remove(key: ITypedKey<V>): V

    /**
     * Removes a value associated with the provided key. Returns this value or null if it does not exist.
     */
    fun <V> removeIfSet(key: ITypedKey<V>): V?
}
