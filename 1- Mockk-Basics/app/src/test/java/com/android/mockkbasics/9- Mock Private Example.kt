package com.android.mockkbasics

import io.mockk.every
import io.mockk.spyk
import io.mockk.verifySequence
import junit.framework.TestCase.assertEquals
import org.junit.Test

class Car {
    fun drive() = accelerate()
    private fun accelerate() = "going faster"

}

class MockPrivateExample {

    // Properties mocking:

    @Test
    fun mockPrivateFunctions() {

        val mock = spyk(Car(), recordPrivateCalls = true)
        every { mock["accelerate"]() } returns "going not so fast"
        assertEquals("going not so fast", mock.drive())

        verifySequence {
            mock.drive()
            mock["accelerate"]()
        }
    }
}