package com.android.mockkbasics

import io.mockk.every
import io.mockk.mockkObject
import junit.framework.TestCase.assertEquals
import org.junit.Test


enum class Enumeration(val goodInt: Int) {
    CONSTANT(35),
    OTHER_CONSTANT(45);
}

// Enums can be mocked using mockkObject:
class EnumerationMocksExample {

    @Test
    fun mockEnum() {
        mockkObject(Enumeration.CONSTANT)
        every { Enumeration.CONSTANT.goodInt } returns 42
        assertEquals(42, Enumeration.CONSTANT.goodInt)
    }
}