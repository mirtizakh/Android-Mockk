package com.android.mockkbasics

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Test

class Every {
    @Test
    fun givenServiceMock_whenCallingMockedMethod_thenCorrectlyVerified() {
        // given
        val service = mockk<TestableService>()
        every { service.getDataFromDb("Expected Param") } returns "Expected Output"

        // when
        val result = service.getDataFromDb("Expected Param")

        // then
        verify { service.getDataFromDb("Expected Param") }
        assertEquals("Expected Output", result)
    }

    /*
     Explanation
     To define the mock object, we’ve used the mockk<…>() method.

     In the next step, we defined the behavior of our mock. For this purpose,
     we’ve created an every block that describes what response should be returned for which call.

     Finally, we used the verify block to verify whether the mock was invoked as we expected.
     */

}