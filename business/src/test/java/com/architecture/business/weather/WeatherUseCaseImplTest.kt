package com.architecture.business.weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.architecture.business.core.exception.BusinessException
import com.architecture.business.core.exception.TechnicalException
import com.architecture.business.core.exception.UnknownException
import com.architecture.cleanmvvm.node1.demo.callback.WeatherCallBack
import com.architecture.cleanmvvm.node1.demo.info.WeatherInfo
import com.architecture.cleanmvvm.node1.demo.repository.WeatherRepository
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherRequest
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherUseCaseImpl
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import kotlin.coroutines.CoroutineContext


@RunWith(MockitoJUnitRunner::class)
class WeatherUseCaseImplTest {
    @Rule
    @JvmField
    public var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var weatherUseCase: WeatherUseCaseImpl

    @Mock
    lateinit var weatherRepository: WeatherRepository

    val testContext: CoroutineContext = Unconfined

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        weatherUseCase = WeatherUseCaseImpl(weatherRepository)
    }

    @Test
    fun shouldCallbackSuccessWhenRepoReturnSuccessData() = runBlocking {
        doReturn(WeatherInfo()).`when`(weatherRepository).invoke()
        val callback = mockCallback<WeatherInfo>()
        excuseTest(callback)
        verify(callback).onSuccess(ArgumentMatchers.isA(WeatherInfo::class.java))
    }

    @Test
    fun shouldCallbackFailByDeFaultWhenRepoReturnReturnFailByDeFault() = runBlocking {
        doAnswer {
            throw UnknownException()
        }.`when`(weatherRepository).invoke()
        val callback = mockCallback<WeatherInfo>()
        excuseTest(callback)
        verify(callback).onDefaultFail(any())
    }

    @Test
    fun shouldCallbackFailByTechnicalWhenRepoReturnReturnFailTechnical() = runBlocking {
        doAnswer {
            throw TechnicalException()
        }.`when`(weatherRepository).invoke()
        val callback = mockCallback<WeatherInfo>()
        excuseTest(callback)
        verify(callback).onFailByTechnical(any())
    }

    @Test
    fun shouldReturnCityNotFoundWhenRepoReturnReturnFailTechnical() = runBlocking {
        doAnswer {
            throw BusinessException()
        }.`when`(weatherRepository).invoke()
        val callback = mockCallback<WeatherInfo>()
        excuseTest(callback)
        verify(callback).onCityNotFound(any())
    }

    private fun excuseTest(callback: WeatherCallBack<WeatherInfo>) {
        weatherUseCase.buildUseCase(WeatherRequest())
        weatherUseCase.subscriberContext = testContext
        weatherUseCase.observerContext = testContext
        weatherUseCase.invoke(callback)
    }

    private fun <T> mockCallback(): WeatherCallBack<T> {
        return Mockito.mock(WeatherCallBack::class.java) as WeatherCallBack<T>
    }
}