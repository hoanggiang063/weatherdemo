package com.architecture.repository.weather.local

import android.database.sqlite.SQLiteCantOpenDatabaseException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.architecture.business.core.exception.BusinessException
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherRequest
import com.architecture.repository.weather.local.model.WeatherEntity
import com.architecture.repository.weather.local.model.WeatherItemEntity
import com.architecture.repository.weather.local.model.WeatherWithDetail
import com.architecture.repository.weather.local.repository.WeatherLocalImpl
import com.architecture.repository.weather.local.service.WeatherDao
import com.nhaarman.mockitokotlin2.any
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
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class WeatherLocalImplTest {
    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var expectedException = ExpectedException.none()

    @Mock
    lateinit var repository: WeatherLocalImpl

    @Mock
    lateinit var dao: WeatherDao

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = WeatherLocalImpl(dao)
        repository.setParam(WeatherRequest())
    }

    @Test
    fun shouldReturnDataWhenDaoHasData() {
        val verifyId = "id"
        val weatherInfoInput = WeatherEntity()
        weatherInfoInput.id = verifyId

        val items = listOf<WeatherItemEntity>(WeatherItemEntity())
        val joinWeatherData = WeatherWithDetail(weatherInfoInput, items)

        runBlocking {
            doReturn(listOf(joinWeatherData)).`when`(dao).getWeatherWithFullDetail(any())
            val weatherInfoOutPut = repository.invoke()
            Assert.assertEquals(weatherInfoOutPut.id, verifyId)
        }
    }

    @Test
    @Throws(Throwable::class)
    fun shouldThrowFailWhenDaoEmpty() {
        expectedException.expect(BusinessException::class.java)
        runBlocking {
            val detail = emptyList<WeatherWithDetail>()
            doReturn(detail).`when`(dao).getWeatherWithFullDetail(any())
            repository.invoke()
        }
    }

    @Test
    @Throws(Throwable::class)
    fun shouldThrowFailWhenDaoThrowException() {
        expectedException.expect(BusinessException::class.java)
        runBlocking {
            doAnswer {
                throw SQLiteCantOpenDatabaseException()
            }.`when`(dao).getWeatherWithFullDetail(any())
            repository.invoke()
        }
    }
}