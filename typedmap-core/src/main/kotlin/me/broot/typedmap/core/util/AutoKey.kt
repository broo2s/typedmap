package me.broot.typedmap.core.util

import me.broot.typedmap.core.api.ITypedKey
import me.broot.typedmap.core.api.MutableTypedMap

/**
 * Classes meant to be stored in [MutableTypedMap] could implement this interface to generate keys for themselves.
 */
interface AutoKey<V> {
    val typedKey: ITypedKey<V>
}

/**
 * Stores the value and identify it by its auto-key.
 *
 * @see setByAutoKey
 * @see MutableTypedMap.set
 * @see AutoKey
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun <V : AutoKey<V>> MutableTypedMap.plusAssign(autoKey: V) = setByAutoKey(autoKey)

/**
 * Stores the value and identify it by its auto-key.
 *
 * @see MutableTypedMap.set
 * @see AutoKey
 */
fun <V : AutoKey<V>> MutableTypedMap.setByAutoKey(autoKey: V) {
    this[autoKey.typedKey] = autoKey
}

