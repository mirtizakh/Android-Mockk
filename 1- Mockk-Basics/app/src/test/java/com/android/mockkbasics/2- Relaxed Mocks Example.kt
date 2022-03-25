package com.android.mockkbasics

import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class RelaxedMocksExample {
    /*
    By default mocks are strict. Before passing mock to a code being tested you should set behavior with every block.
    In case you do not provide expected behavior, and call is performed, library throws an exception.
    This is different from what Mockito is doing by default.
    Mockito allows you to skip specifying expected behavior and replies with some basic value alike null or 0.
    You can achieve the same and even more in MockK by declaring relaxed mock.
     */

    @Test
    fun relaxedMock() {
        val mock = mockk<Divider>(relaxed = true)

        mock.divide(5, 2) // returns 0

        // In verify blocks you can check if that calls were performed:

        verify { mock.divide(5, 2) }
    }

    @Test
    fun relaxedChainMock() {
        val mock = mockk<Divider>(relaxed = true)

        /* Besides that, if the return value is of reference type, library would try to create child mock
          and build chain of calls.
         */
        mock.call1(1, 2).call2(4, 5)// returns 0

        // In verify blocks you can check if that calls were performed:

        verify { mock.call1(1, 2).call2(4, 5) }
    }

    // Mock relaxed for functions returning Unit

    /*
    n case you would like Unit returning functions to be relaxed,
    you can use relaxUnitFun = true as an argument to the mockk function,
     @MockK annotation or MockKAnnotations.init function.
     */

}