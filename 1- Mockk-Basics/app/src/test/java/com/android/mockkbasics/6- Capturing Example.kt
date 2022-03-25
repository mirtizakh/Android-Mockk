package com.android.mockkbasics

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CapturingExample {
    /* Argument capturing can make your life easier if you need to get a value of an argument
     in every or verify block.

     There are two ways to capture arguments: using CapturingSlot<Int> and using MutableList<Int>.
     */

    // CapturingSlot allows to capture only one value, so it is simpler to use.
    @Test
    fun capturingSlot() {
        val slot = slot<Int>()
        val mock = mockk<Divider>()
        every { mock.divide(capture(slot), any()) } returns 22

        /*
        This creates a slot and a mock and set expected behavior following way:
        in case mock.divide is called, then first argument is captured to the slot and 22 is returned.
        Now code being tested:
         */

        mock.divide(5, 2) // 22 is a result

        // After executing it, slot.captured value is equal to first argument i.e. 5
        // Now you can do some checks, assert for example:

        assertEquals(5, slot.captured)

        // Besides that, you can use a slot in an answer lambda:

        every {
            mock.divide(capture(slot), any())
        } answers {
            slot.captured * 11
        }

        val result = mock.divide(5, 2)   // returns 55.

        assertEquals(55, result)
    }

    @Test
    fun capturingWithMutableList() {
        val list = mutableListOf<Int>()
        val mock = mockk<Divider>()
        every { mock.divide(capture(list), any()) } returns 22

        // This allows capturing several values from several calls in a row.

        mock.divide(5, 2) // 22 is a result
        mock.divide(2, 5) // 22 is a result
        mock.divide(9, 15) // 22 is a result

        assertEquals(5, list[0])
        assertEquals(2, list[1])
        assertEquals(9, list[2])
    }
}