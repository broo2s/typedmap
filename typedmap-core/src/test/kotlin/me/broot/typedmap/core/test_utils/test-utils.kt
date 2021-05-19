package me.broot.typedmap.core.test_utils

import kotlin.reflect.KType
import kotlin.reflect.jvm.ExperimentalReflectionOnLambdas
import kotlin.reflect.jvm.reflect
import kotlin.reflect.typeOf
import kotlin.test.assertEquals

@OptIn(ExperimentalReflectionOnLambdas::class)
fun <T> compileType(expr: () -> T): KType {
    return checkNotNull(expr.reflect()?.returnType)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T> assertType(actual: KType) {
    assertEquals(typeOf<T>(), actual)
}
