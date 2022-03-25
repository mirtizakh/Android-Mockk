package com.android.mockkbasics

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CalculationPartialArgumentMatching {
    fun add(num1: Int, num2: Int, num3: Int) = num1 + num2 + num3
}
class PartialArgumentMatching {

    @Test
    fun partialArgumentMatching(){
        val mock = mockk<CalculationPartialArgumentMatching>()

        every {
            mock.add(
                num1 = more(50),
                num2 = 30, // here eq() is used
                num3 = any()
            )
        } returns 100

        val result = mock.add(60, 30, 40)

        assertEquals(100,result)
        verify { mock.add(60, 30, 40)}

        confirmVerified(mock)
    }
}