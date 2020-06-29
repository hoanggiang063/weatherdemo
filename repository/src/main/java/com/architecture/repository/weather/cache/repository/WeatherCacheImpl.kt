package com.architecture.repository.weather.cache.repository

import com.architecture.business.core.exception.BusinessException
import com.architecture.business.core.info.Undefine
import com.architecture.cleanmvvm.node1.demo.info.WeatherInfo
import com.architecture.cleanmvvm.node1.demo.repository.WeatherRepository
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherRequest
import com.architecture.repository.weather.local.repository.WeatherLocalImpl
import com.architecture.repository.weather.remote.repository.WeatherRemoteImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class WeatherCacheImpl(
    var forceRefresh: Boolean,
    val localDataGetting: WeatherLocalImpl,
    val remoteDataGetting: WeatherRemoteImpl

) : WeatherRepository {

    private lateinit var request: WeatherRequest

    fun shouldFetch(data: WeatherInfo?): Boolean {
        return data == null || Undefine.UNDEFINE_STRING.equals(data.id) || forceRefresh
    }

    override fun setParam(param: WeatherRequest) {
        localDataGetting.setParam(param)
        remoteDataGetting.setParam(param)
        request = param
    }

    override suspend fun invoke(): WeatherInfo {
        var result: WeatherInfo? = null
        try {
            result = localDataGetting()
        } catch (exception: Throwable) {
            // don't have data and fetch data
        }

        if (shouldFetch(result)) {
            CoroutineScope(Dispatchers.Default).async {
                result = remoteDataGetting()
            }.await()

            result?.let {
                localDataGetting.saveWeather(result!!, request)
                result = localDataGetting()
            }
        }

        result?.let {
            return result as WeatherInfo
        }

        val businessException = BusinessException()
        businessException.businessCode = BusinessException.DEFAULT_DB_ERROR_CODE
        businessException.businessMessage = BusinessException.DEFAULT_DB_ERROR_MESSAGE
        throw businessException
    }

}