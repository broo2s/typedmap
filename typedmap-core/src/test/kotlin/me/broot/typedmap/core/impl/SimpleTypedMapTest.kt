package me.broot.typedmap.core.impl

import me.broot.typedmap.core.AbstractMutableTypedMapTest
import me.broot.typedmap.core.api.MutableTypedMap
import kotlin.test.Test

@Test
class SimpleTypedMapTest : AbstractMutableTypedMapTest<MutableTypedMap>() {
    override fun createInstance() = simpleTypedMap()
}
