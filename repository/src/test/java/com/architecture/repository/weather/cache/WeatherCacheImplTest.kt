package com.architecture.repository.weather.cache

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.architecture.business.core.exception.BusinessException
import com.architecture.business.core.exception.TechnicalException
import com.architecture.business.core.exception.UnknownException
import com.architecture.cleanmvvm.node1.demo.info.WeatherInfo
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherRequest
import com.architecture.repository.weather.cache.repository.WeatherCacheImpl
import com.architecture.repository.weather.local.repository.WeatherLocalImpl
import com.architecture.repository.weather.remote.repository.WeatherRemoteImpl
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class WeatherCachesImplTest {
    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var expectedException = ExpectedException.none()

    @Mock
    lateinit var repository: WeatherCacheImpl

    @Mock
    lateinit var dbRepo: WeatherLocalImpl

    @Mock
    lateinit var remoteRepo: WeatherRemoteImpl

    private fun <T> anyObject(): T {
        Mockito.anyObject<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository =
            WeatherCacheImpl(
                false,
                dbRepo,
                remoteRepo
            )
        repository.setParam(WeatherRequest())
    }

    @Test
    fun shouldReturnDataWhenUseLocal() {
        val weatherInfo = WeatherInfo()
        weatherInfo.id = "verifyId"
        runBlocking {
            doReturn(weatherInfo).`when`(dbRepo).invoke()
            val weatherInfoOutPut = repository.invoke()
            Assert.assertEquals(weatherInfoOutPut.id, weatherInfo.id)
        }
    }

    @Test
    fun shouldReturnDataWhenUseRemote() {
        repository.forceRefresh = true
        val weatherInfo = WeatherInfo()
        weatherInfo.id = "verifyId"
        runBlocking {
            doReturn(WeatherInfo()).doReturn(weatherInfo).`when`(dbRepo).invoke()
            doReturn(weatherInfo).`when`(remoteRepo).invoke()
            `when`(dbRepo.saveWeather(anyObject(), anyObject())).then { }
            val weatherInfoOutPut = repository.invoke()
            Assert.assertEquals(weatherInfoOutPut.id, weatherInfo.id)
        }
    }

    @Test
    @Throws(Throwable::class)
    fun shouldThrowTechnicalExceptionWhenRemoteReturnTechnicalException() {
        expectedException.expect(TechnicalException::class.java)

        repository.forceRefresh = true

        runBlocking {
            doReturn(WeatherInfo()).`when`(dbRepo).invoke()
            doAnswer {
                throw TechnicalException()
            }.`when`(remoteRepo).invoke()

            repository.invoke()
        }
    }

    @Test
    @Throws(Throwable::class)
    fun shouldThrowBusinessExceptionWhenRemoteReturnBusinessException() {
        expectedException.expect(BusinessException::class.java)

        repository.forceRefresh = true
        val weatherInfo = WeatherInfo()
        weatherInfo.id = "verifyId"

        runBlocking {
            doReturn(WeatherInfo()).`when`(dbRepo).invoke()
            doAnswer {
                throw BusinessException()
            }.`when`(remoteRepo).invoke()
            repository.invoke()
        }
    }

    @Test
    @Throws(Throwable::class)
    fun shouldThrowUnknownExceptionWhenRemoteReturnUnknownException() {
        expectedException.expect(UnknownException::class.java)

        repository.forceRefresh = true

        runBlocking {
            doReturn(WeatherInfo()).`when`(dbRepo).invoke()
            doAnswer {
                throw UnknownException()
            }.`when`(remoteRepo).invoke()

            repository.invoke()
        }
    }

}