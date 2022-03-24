package com.android.mockkbasics

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import org.junit.Before
import org.junit.Test

class TrafficSystem {
    lateinit var car1: Car

    lateinit var car2: Car

    lateinit var car3: Car
}

class CarTest {
    @MockK
    lateinit var car1: Car

    @RelaxedMockK
    lateinit var car2: Car

    @MockK(relaxUnitFun = true)
    lateinit var car3: Car

    @SpyK
    var car4 = Car()

    @InjectMockKs
    var trafficSystem = TrafficSystem()

    @Before
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks

    /*
     Injection first tries to match properties by name, then by class or superclass.
     Check the lookupType parameter for customization.

     Properties are injected even if private is applied.
     Constructors for injection are selected from the biggest number of arguments to lowest.

     @InjectMockKs by default is injecting only lateinit vars or vars that are not assigned. To change this,
     use overrideValues = true. This would assign the value even if it is already somehow initialized.
     To inject vals, use injectImmutable = true. For a shorter notation use @OverrideMockKs
     which does the same as @InjectMockKs by default, but turns these two flags on.
     */
    @Test
    fun calculateAddsValues1() {
        // ... use car1, car2, car3 and car4
    }
}