package com.android.mockkbasics

import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Test

class SpiesExample {
    /*
    Spies give the possibility to set expected behavior and do behavior verification while still executing
    original methods of an object.
     */

    @Test
    fun spyExample() {
        val spy = spyk<Adder>()

        /*
        Here we create object Adder() and build a spy on top of it.
        Building a spy actually means creating a special empty object of the same type and copying all the fields.
        Now we can use it, as if it was regular Adder() object.
        */

        assertEquals(9, spy.add(4, 5))

        verify(exactly = 1) { spy.magnify(any()) }

        // The above code checks that original method is called.

        // Besides that, we can define spy behavior by putting it to every block:

        every { spy.magnify(any()) } answers { firstArg<Int>() * 2 }

        /*
        This provides an expected answer for magnify function.
        After that, behavior of add has changed because it was dependent on magnify:
         */

        assertEquals(14, spy.add(4, 5))
    }
}