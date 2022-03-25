package com.android.mockkbasics

import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test

class Calculation {
    fun addOne(num: Int) = num + 1
}

class PartialMockingExample {
    /*
    Sometimes, you need to stub some functions, but still call the real method on others, or on specific arguments.
    This is possible by passing callOriginal() to answers, which works for both relaxed and non-relaxed mocks.
     */

    @Test
    fun partialMocking() {
        val adder = mockk<Calculation>()

        every { adder.addOne(any()) } returns -1
        every { adder.addOne(3) } answers { callOriginal() }

        assertEquals(-1, adder.addOne(2))
        assertEquals(4, adder.addOne(3)) // original function is called
    }
}