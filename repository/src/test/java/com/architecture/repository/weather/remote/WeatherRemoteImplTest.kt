package com.architecture.repository.weather.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.architecture.business.core.exception.TechnicalException
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherRequest
import com.architecture.repository.core.mapper.HttpStatus
import com.architecture.repository.weather.remote.model.WeatherModel
import com.architecture.repository.weather.remote.repository.WeatherRemoteImpl
import com.architecture.repository.weather.remote.service.WeatherRemoteService
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response
import java.io.BufferedReader
import java.io.File
import java.io.IOException


@RunWith(MockitoJUnitRunner::class)
class WeatherRemoteImplTest {
    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var expectedException = ExpectedException.none()

    @Mock
    lateinit var repository: WeatherRemoteImpl

    @Mock
    lateinit var service: WeatherRemoteService

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = WeatherRemoteImpl(service)
        repository.setParam(WeatherRequest())
    }

    @Test
    fun shouldReturnDataWhenApiSuccess() = runBlocking {
        val file = File(javaClass.classLoader.getResource("test.json").file)
        val bufferedReader: BufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use { it.readText() }
        val gson = Gson()
        val weatherInfoInput = gson.fromJson(inputString, WeatherModel::class.java)
        doReturn(weatherInfoInput).`when`(service).getWeather(any(), any(), any(), any())
        val weatherInfoOutPut = repository.invoke()

        Assert.assertEquals(weatherInfoOutPut.id, weatherInfoInput.city.id)
        // should evaluate more values but no time
    }

    @Test
    @Throws(Throwable::class)
    fun shouldThrowFailWhenApiReturnIoException() {
        expectedException.expect(TechnicalException::class.java)
        runBlocking {
            doAnswer {
                throw IOException()
            }.`when`(service).getWeather(any(), any(), any(), any())
            repository.invoke()
        }
    }

    @Test
    @Throws(Throwable::class)
    fun shouldThrowFailWhenApiReturnHttpException() {
        expectedException.expect(TechnicalException::class.java)
        runBlocking {
            doAnswer {
                throw getMockHttpException()
            }.`when`(service).getWeather(any(), any(), any(), any())
            repository.invoke()
        }
    }

    @Test
    @Throws(Throwable::class)
    fun shouldThrowFailWhenApiReturnBusinessException() {
        expectedException.expect(TechnicalException::class.java)
        runBlocking {
            doAnswer {
                throw getMockHttpBusinessException()
            }.`when`(service).getWeather(any(), any(), any(), any())
            repository.invoke()
        }
    }

    private fun getMockHttpBusinessException(): HttpException {
        val body = ResponseBody.create(
            "UTF8".toMediaTypeOrNull(),
            "{\"cod\":\"403\",\"message\":\"city not found\"}"
        )
        val response = Response.error<Any>(HttpStatus.FORBIDDEN.value(), body)
        val exception = HttpException(response)
        return exception
    }

    private fun getMockHttpException(): HttpException {
        val body = ResponseBody.create("UTF8".toMediaTypeOrNull(), "")
        val response = Response.error<Any>(HttpStatus.GATEWAY_TIMEOUT.value(), body)
        val exception = HttpException(response)
        return exception
    }
}