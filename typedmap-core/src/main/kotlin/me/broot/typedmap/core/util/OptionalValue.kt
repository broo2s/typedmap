@file:Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
@file:OptIn(ExperimentalContracts::class)

package me.broot.typedmap.core.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Optional/Maybe type for Kotlin.
 *
 * Simple immutable value wrapper with one of three possible states:
 *
 * - non-null value,
 * - null value,
 * - absent/unset/undefined/unspecified/missing.
 *
 * This is useful when we need to store information whether some variable/value was specified or not and at the same
 * time this variable is nullable, so we can't use `null` as "not specified". One example is parsing of JSON if we
 * need to distinguish latter two cases:
 *
 * - `{"foo": "bar"}`
 * - `{"foo": null}`
 * - `{}`
 */
@JvmInline
public value class OptionalValue<out V> @PublishedApi internal constructor(
    @PublishedApi
    internal val value: Any?
) {
    public companion object {
        public inline fun <V> of(value: V): OptionalValue<V> = OptionalValue(value)
        public inline fun absent(): OptionalValue<Nothing> = OptionalValue(Absent)
        public inline fun <V : Any> ofNullable(value: V?): OptionalValue<V> =
            if (value == null) absent() else of(value)
    }

    public inline val isPresent: Boolean get() = value !== Absent
    public inline val isAbsent: Boolean get() = value === Absent

    public inline fun get(): V = getOrElse { throw NoSuchElementException() }
    public inline fun getOrNull(): V? = getOrElse { null }

    override fun toString(): String {
        return if (isPresent) {
            "OptionalValue.of($value)"
        } else {
            "OptionalValue.absent()"
        }
    }

    @PublishedApi
    internal object Absent
}

public inline fun <V> OptionalValue<V>.getOrElse(onAbsent: () -> V): V {
    contract {
        callsInPlace(onAbsent, InvocationKind.AT_MOST_ONCE)
    }
    return map({ return@map it }, onAbsent)
}

public inline fun <V, R> OptionalValue<V>.mapValue(transform: (V) -> R): OptionalValue<R> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return map({ OptionalValue.of(transform(it)) }, { OptionalValue.absent() })
}

public inline fun <V> OptionalValue<V>.ifPresent(onPresent: (V) -> Unit) {
    contract {
        callsInPlace(onPresent, InvocationKind.AT_MOST_ONCE)
    }
    map(onPresent) {}
}

public inline fun OptionalValue<*>.ifAbsent(onAbsent: () -> Unit) {
    contract {
        callsInPlace(onAbsent, InvocationKind.AT_MOST_ONCE)
    }
    map({}, onAbsent)
}

public inline fun <V, R> OptionalValue<V>.map(onPresent: (V) -> R, onAbsent: () -> R): R {
    contract {
        callsInPlace(onPresent, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onAbsent, InvocationKind.AT_MOST_ONCE)
    }
    return if (isPresent) onPresent(value as V) else onAbsent()
}

public fun <K, V> Map<K, V>.getOptional(key: K): OptionalValue<V> {
    val value = this[key]
    return if (value != null || containsKey(key)) {
        OptionalValue.of(value as V)
    } else {
        OptionalValue.absent()
    }
}
