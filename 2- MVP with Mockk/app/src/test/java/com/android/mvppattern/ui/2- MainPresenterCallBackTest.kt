package com.android.mvppattern.ui

import com.android.mvppattern.data.DataRepository
import com.android.mvppattern.domain.DataModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainPresenterCallBackTest {
    /*
    In this first sample we want to:
    Mock a response with a list
    Verify onSuccess() is called
    Verify the output is not an empty list
     */

    @RelaxedMockK
    lateinit var view: MainContract.View

    @RelaxedMockK
    lateinit var dataRepository: DataRepository

    lateinit var mainPresenter: MainContract.Presenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mainPresenter = MainPresenter(view, dataRepository)
    }

    @Test
    fun fetchDataWithAResultViaCallBack() {
        val captureCallback = slot<(data: List<DataModel>) -> Unit>()
        every { dataRepository.fetchDataViaCallBack(capture(captureCallback)) } answers {
            val fakeList = listOf(DataModel(1, "Value 1"))
            captureCallback.captured.invoke(fakeList)
        }
        mainPresenter.fetchDataViaCallback()

        val captureData = slot<List<UiDataModel>>()
        verify(exactly = 1) { dataRepository.fetchDataViaCallBack(capture(captureCallback)) }
        verify(exactly = 1) { view.onResult(capture(captureData)) }

        captureData.captured.let { res ->
            assertNotNull(res)
            assert(res.isNotEmpty())
            assertEquals("Value 1", res.first().value)
        }
    }

    @Test
    fun fetchDataWithAResultViaCallBack2() {
        val captureCallback = slot<(data: List<DataModel>) -> Unit>()
        val fakeList = listOf(DataModel(1, "Value 1"))

        mainPresenter.fetchDataViaCallback()

        verify(exactly = 1) { dataRepository.fetchDataViaCallBack(capture(captureCallback)) }
        captureCallback.captured.invoke(fakeList)

        val captureData = slot<List<UiDataModel>>()

        verify(exactly = 1) { view.onResult(capture(captureData)) }

        captureData.captured.let { res ->
            assertNotNull(res)
            assert(res.isNotEmpty())
            assertEquals("Value 1", res.first().value)
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    /*
    In this test you can see we are using the capture() method, by using it(and a slot<>() variable)
    we can intercept the callback passed inside the Presenter and provide to it the result.
    The slot<>() is a simple container where save the intercepted value.
    The capture slot is also used to intercept the result of a method to do an assertion.
     */
}