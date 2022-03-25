package com.android.mockkbasics

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

/*
The library supports annotations @MockK, @SpyK and @RelaxedMockK,
which can be used as a simpler way to create mocks, spies, and relaxed mocks correspondingly.

 */
class Annotation {
    @MockK
    lateinit var doc1: Dependency1

    @RelaxedMockK
    lateinit var doc2: Dependency2

    @SpyK
     var doc2Spy = Dependency2("10")

    @Before
    fun setUp() = MockKAnnotations.init(this)

    /*
     The important part here is MockKAnnotations.init(this) call which is executed at @Before phase.
     When it is executed all annotated properties are substituted with corresponding objects: mocks, spies and relaxed mocks.
     */

    @Test
    fun calculateAddsValues1() {
        every { doc1.call(10)} returns 5
        // Since doc2 is a relaxed mock so if we don't write behaviour block for it the return will be zero
        // every { doc2.call(6) } returns 6


        val sut = SystemUnderTest(doc1,doc2)
        assertEquals(5, sut.calculateWithCall(10,6))  // because relaxed mock returns zero

        verify { doc1.call(any()) }
        verify { doc2.call(any()) }

        val sutWithSpy = SystemUnderTest(doc1,doc2Spy)
        assertEquals(11, sutWithSpy.calculateWithCall(10,6))

        verify { doc1.call(any()) }
        verify { doc2Spy.call(any()) }
        verify { doc2Spy.result(any()) }
    }
}