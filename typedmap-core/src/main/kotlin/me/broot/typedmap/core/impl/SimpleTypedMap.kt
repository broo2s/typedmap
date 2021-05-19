package me.broot.typedmap.core.impl

import me.broot.typedmap.core.api.ITypedKey
import me.broot.typedmap.core.api.MutableTypedMap
import me.broot.typedmap.core.util.OptionalValue
import me.broot.typedmap.core.util.getOptional
import me.broot.typedmap.core.util.getOrElse

/**
 * Creates an empty [MutableTypedMap].
 *
 * This is a basic implementation of `MutableTypedMap`. It does not provide any advanced features like e.g. polymorphic
 * value access.
 */
fun simpleTypedMap(): MutableTypedMap = SimpleTypedMap()

internal class SimpleTypedMap : MutableTypedMap {
    private val map = mutableMapOf<ITypedKey<Any?>, Any?>()

    override fun <V> set(key: ITypedKey<V>, value: V) {
        @Suppress("UNCHECKED_CAST")
        map[key as ITypedKey<Any?>] = value
    }

    override fun <V> remove(key: ITypedKey<V>): V {
        @Suppress("UNCHECKED_CAST")
        if (!map.containsKey(key as ITypedKey<Any?>)) {
            throw NoSuchElementException("Tried to remove a key which is missing: $key")
        }

        @Suppress("UNCHECKED_CAST")
        return map.remove(key) as V
    }

    override fun <V> removeIfSet(key: ITypedKey<V>): V? {
        @Suppress("UNCHECKED_CAST")
        return map.remove(key as ITypedKey<Any?>) as V?
    }

    override fun <V> get(key: ITypedKey<V>): V {
        return getOptional(key).getOrElse { throw NoSuchElementException("Key is missing: $key") }
    }

    override fun <V> getOrNull(key: ITypedKey<V>): V? {
        @Suppress("UNCHECKED_CAST")
        return map[key as ITypedKey<Any?>] as V?
    }

    override fun <V> contains(key: ITypedKey<V>): Boolean {
        @Suppress("UNCHECKED_CAST")
        return map.contains(key as ITypedKey<Any?>)
    }

    override fun <V> getOptional(key: ITypedKey<V>): OptionalValue<V> {
        @Suppress("UNCHECKED_CAST")
        return map.getOptional(key as ITypedKey<Any?>) as OptionalValue<V>
    }
}
