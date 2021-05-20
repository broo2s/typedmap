package me.broot.typedmap.core.util

import me.broot.typedmap.core.api.ITypedKey
import me.broot.typedmap.core.api.MutableTypedMap

/**
 * Classes meant to be stored in [MutableTypedMap] could implement this interface to generate keys for themselves.
 */
public interface AutoKey<V> {
    public val typedKey: ITypedKey<V>
}

/**
 * Stores the value and identify it by its auto-key.
 *
 * @see setByAutoKey
 * @see MutableTypedMap.set
 * @see AutoKey
 */
@Suppress("NOTHING_TO_INLINE")
public inline operator fun <V : AutoKey<V>> MutableTypedMap.plusAssign(autoKey: V) : Unit = setByAutoKey(autoKey)

/**
 * Stores the value and identify it by its auto-key.
 *
 * @see MutableTypedMap.set
 * @see AutoKey
 */
public fun <V : AutoKey<V>> MutableTypedMap.setByAutoKey(autoKey: V) {
    this[autoKey.typedKey] = autoKey
}

