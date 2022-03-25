package com.android.mockkbasics

import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test

class Bar {
    lateinit var nickname: String
}

class Foo {
    lateinit var name: String
    lateinit var bar: Bar
}


class HierarchicalMockingExample {

    @Test
    fun hierarchicalMocking() {
        // given
        val foo = mockk<Foo> {
            every { name } returns "Karol"
            every { bar } returns mockk {
                every { nickname } returns "Tomato"
            }
        }

        // when
        val name = foo.name
        val nickname = foo.bar.nickname

        // then
        assertEquals("Karol", name)
        assertEquals("Tomato", nickname)
    }
}