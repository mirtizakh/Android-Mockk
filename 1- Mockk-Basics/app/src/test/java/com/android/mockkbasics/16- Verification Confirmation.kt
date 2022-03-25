package com.android.mockkbasics

import io.mockk.*
import org.junit.Test

class CalculationVerificationConfirmation {
    fun add(num1: Int) = num1 + 10
}

class VerificationConfirmation{

    // To double check that all calls were verified by verify... constructs, you can use confirmVerified:
    // confirmVerified(mock1, mock2)

    /*
    It does not make much sense to use it for verifySequence and verifyAll as these verification methods
     already exhaustively cover all calls with verification.

    It will throw an exception in case there are some calls left without verification.

    Some calls may be skipped from such confirmation, check the next section for more details.
     */
    @Test
    fun verificationConfirmation(){
        val mock = mockk<CalculationVerificationConfirmation>()

        every { mock.add(10) } returns 20
        every { mock.add(20) } returns 30

        mock.add(10) // returns 20
        mock.add(20) // returns 30

        verify {
            mock.add(10)
            mock.add(20)
        }

        confirmVerified(mock)
    }

    // Recording exclusions

    //To exclude some not so important calls from being recorded you can use excludeRecords:

    // All matching calls will be excluded from recording.
    // This may be useful in case you are using exhaustive verification: verifyAll, verifySequence or confirmVerified.

    @Test
    fun recordingExclusions(){
        val mock = mockk<CalculationVerificationConfirmation>()

        every { mock.add(10) } returns 20
        every { mock.add(20) } returns 30

        excludeRecords { mock.add(20) }

        mock.add(10) // returns 20
        mock.add(20) // returns 30

        verify {
            mock.add(10)
        }

        confirmVerified(mock)
    }

    // Verification timeout

    //To verify concurrent operations, you can use timeout = xxx:

    @Test
    fun verificationTimeout(){
        mockk<CalculationVerificationConfirmation>() {
            every { add(10) } returns 20
            Thread {
                Thread.sleep(2000)
                add(10)
            }.start()

            verify(timeout = 3000) { add(10) }
        }
    }
    // This will wait until one of two following states: either verification is passed or timeout is reached.
}