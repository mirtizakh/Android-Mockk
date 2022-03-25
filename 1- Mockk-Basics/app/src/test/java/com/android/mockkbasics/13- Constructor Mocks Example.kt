package com.android.mockkbasics

import io.mockk.*
import junit.framework.TestCase.assertEquals
import org.junit.Test


class MockCls {
    fun add(a: Int, b: Int) = a + b
}

class ConstructorMocksExample {

    @Test
    fun mockConstructor(){
        mockkConstructor(MockCls::class)

        every { anyConstructed<MockCls>().add(1, 2) } returns 4

        assertEquals(4, MockCls().add(1, 2)) // note new object is created

        verify { anyConstructed<MockCls>().add(1, 2) }
    }
}