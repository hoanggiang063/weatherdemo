package com.architecture.cleanmvvm.weather.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.architecture.business.core.exception.BusinessException
import com.architecture.business.core.exception.TechnicalException
import com.architecture.business.core.exception.UnknownException
import com.architecture.business.core.usecase.BaseUseCase
import com.architecture.cleanmvvm.core.configuration.EnvConfiguration
import com.architecture.cleanmvvm.node1.demo.callback.WeatherCallBack
import com.architecture.cleanmvvm.node1.demo.info.WeatherInfo
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherRequest
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherUseCase
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Job
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {

    @Rule
    @JvmField
    public var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var weatherUseCase: WeatherUseCase

    @Mock
    lateinit var viewModel: WeatherViewModel

    @Mock
    lateinit var envConfiguration: EnvConfiguration

    @Before
    fun setUp() {
        envConfiguration = object : EnvConfiguration {
            override fun getEnvironmentUrl(): String = "url"
            override fun getEnvironmentApiKey(): String = "apikey"
            override fun getEnvironmentUnit(): String = "unit"
            override fun getSpinningKey(): String = "sha1/key"
        }
    }

    @Test
    fun shouldTriggerSuccessWhenUseCaseReturnData() {
        val mockObserver = mockObserver<WeatherInfo>()
        weatherUseCase = object : WeatherUseCase {
            override fun buildUseCase(param: WeatherRequest):
                    BaseUseCase<WeatherRequest, WeatherInfo, WeatherCallBack<WeatherInfo>> = this

            override fun invoke(callback: WeatherCallBack<WeatherInfo>): Job {
                callback.onSuccess(WeatherInfo())
                return Job()
            }
        }

        viewModel = WeatherViewModel(weatherUseCase, envConfiguration);
        viewModel.currentWeatherInfo.observe(mockLifecycleOwner(), mockObserver)
        viewModel.loadWeather("cali")
        verify(mockObserver).onChanged(ArgumentMatchers.isA(WeatherInfo::class.java))
    }

    @Test
    fun shouldTriggerDefaultFailWhenUseCaseReturnDefaultFail() {
        var mockObserver = mockObserver<Throwable>()
        weatherUseCase = object : WeatherUseCase {
            override fun buildUseCase(param: WeatherRequest):
                    BaseUseCase<WeatherRequest, WeatherInfo, WeatherCallBack<WeatherInfo>> = this

            override fun invoke(callback: WeatherCallBack<WeatherInfo>): Job {
                callback.onDefaultFail(UnknownException())
                return Job()
            }
        }
        viewModel = WeatherViewModel(weatherUseCase, envConfiguration);
        viewModel.failedException.observe(mockLifecycleOwner(), mockObserver)
        viewModel.loadWeather("cali")
        verify(mockObserver).onChanged(ArgumentMatchers.isA(Throwable::class.java))
    }

    @Test
    fun shouldTriggerTechnicalFailWhenUseCaseReturnTechnicalFail() {
        var mockObserver = mockObserver<Throwable>()
        weatherUseCase = object : WeatherUseCase {
            override fun buildUseCase(param: WeatherRequest):
                    BaseUseCase<WeatherRequest, WeatherInfo, WeatherCallBack<WeatherInfo>> = this

            override fun invoke(callback: WeatherCallBack<WeatherInfo>): Job {
                callback.onFailByTechnical(TechnicalException())
                return Job()
            }
        }
        viewModel = WeatherViewModel(weatherUseCase, envConfiguration);
        viewModel.failedByTechnical.observe(mockLifecycleOwner(), mockObserver)
        viewModel.loadWeather("cali")
        verify(mockObserver).onChanged(ArgumentMatchers.isA(TechnicalException::class.java))
    }

    @Test
    fun shouldTriggerBusinessFailWhenUseCaseReturnBusinessFail() {
        var mockObserver = mockObserver<Throwable>()
        weatherUseCase = object : WeatherUseCase {
            override fun buildUseCase(param: WeatherRequest):
                    BaseUseCase<WeatherRequest, WeatherInfo, WeatherCallBack<WeatherInfo>> = this

            override fun invoke(callback: WeatherCallBack<WeatherInfo>): Job {
                callback.onCityNotFound(BusinessException())
                return Job()
            }
        }
        viewModel = WeatherViewModel(weatherUseCase, envConfiguration);
        viewModel.failedByBusiness.observe(mockLifecycleOwner(), mockObserver)
        viewModel.loadWeather("cali")
        verify(mockObserver).onChanged(ArgumentMatchers.isA(BusinessException::class.java))
    }

    private fun mockLifecycleOwner(): LifecycleOwner {
        val lifecycleOwner: LifecycleOwner = Mockito.mock(LifecycleOwner::class.java)
        val lifecycle = LifecycleRegistry(Mockito.mock(LifecycleOwner::class.java))
        lifecycle.markState(Lifecycle.State.RESUMED)
        Mockito.`when`(lifecycleOwner.lifecycle).thenReturn(lifecycle)
        return lifecycleOwner
    }

    private fun <T> mockObserver(): Observer<T> {
        return Mockito.mock(Observer::class.java) as Observer<T>
    }
}