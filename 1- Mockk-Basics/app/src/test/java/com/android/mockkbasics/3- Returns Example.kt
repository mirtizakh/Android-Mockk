package com.android.mockkbasics

import io.mockk.*
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ReturnsExample {

    /*
    returnsMany specify a number of values that are used one by one
    i.e. first matched call returns first element, second — second element, e.t.c.
     */
    @Test
    fun calculateAddsValues() {
        val doc1 = mockk<Dependency1>()
        val doc2 = mockk<Dependency2>()

        every { doc1.value1 } returnsMany listOf(1, 2, 3)
        every { doc2.value2 } returns "6"

        val sut = SystemUnderTest(doc1, doc2)

        assertEquals(7, sut.calculate())
        assertEquals(8, sut.calculate())
        assertEquals(9, sut.calculate())
    }

    // You can achieve the same using andThen construct:

    @Test
    fun calculateAddsValuesAndThen() {
        val doc1 = mockk<Dependency1>()
        val doc2 = mockk<Dependency2>()

        every { doc1.value1 } returns 1 andThen 2 andThen 3
        every { doc2.value2 } returns "6"

        val sut = SystemUnderTest(doc1, doc2)

        assertEquals(7, sut.calculate())
        assertEquals(8, sut.calculate())
        assertEquals(9, sut.calculate())
    }


    @Test(expected = RuntimeException::class)
    fun returnException() {
        val doc1 = mockk<Dependency1>()

        every { doc1.call(5) } throws RuntimeException("error happened")

        doc1.call(5)
    }

    /* just Runs should be used in case return value is Unit.
      just run is used for methods returning Unit (i.e., not returning a value) on strict mocks.
      If you create a mock that is not relaxed and invoke a method on it that has not being stubbed with an every block,
      MockK will throw an exception.
    */
    @Test
    fun justReturns() {
        val doc1 = mockk<Dependency1>()
        every { doc1.result(5) } just Runs
        every { doc1.call(5) } returns 5

        /*
          Or we simply create the mock by using relaxUnitFun = true
          val doc1 = mockk<Dependency1>(relaxUnitFun = true)

          so we don't need to add behavior , you can replace line 62 and 63 by line 68
         */

        doc1.call(5)
        doc1.result(5)

        verify(exactly = 1) { doc1.result(5) }
        verify(exactly = 1) { doc1.call(5) }
    }


    // answers allow specifying custom lambda function returning an answer.
    // Check the MVP project in the repository to get better understanding of answer
    @Test
    fun answerReturns() {
        val doc1 = mockk<Dependency1>()
        every { doc1.call(5) } answers { arg<Int>(0) + 5 }

        /*
         arg<Int>(0) stands for the value of the first argument in an intercepted call.
          There is a bunch of such properties and functions available in the scope of answer lambda.
         */

        val result = doc1.call(5)

        assertEquals(10, result)
    }
}