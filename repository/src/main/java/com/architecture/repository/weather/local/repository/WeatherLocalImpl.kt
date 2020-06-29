package com.architecture.repository.weather.local.repository

import com.architecture.business.core.exception.BusinessException
import com.architecture.cleanmvvm.node1.demo.info.WeatherInfo
import com.architecture.cleanmvvm.node1.demo.info.WeatherItemInfo
import com.architecture.cleanmvvm.node1.demo.repository.WeatherRepository
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherRequest
import com.architecture.repository.core.mapper.BaseInfoMapper
import com.architecture.repository.weather.local.model.WeatherEntity
import com.architecture.repository.weather.local.model.WeatherItemEntity
import com.architecture.repository.weather.local.model.WeatherWithDetail
import com.architecture.repository.weather.local.service.WeatherDao

class WeatherLocalImpl(val dao: WeatherDao) : WeatherRepository {

    var request: WeatherRequest? = null

    override fun setParam(param: WeatherRequest) {
        request = param
    }

    override suspend fun invoke(): WeatherInfo {
        try {
            val weatherWithDetails = dao.getWeatherWithFullDetail(request!!.city)
            if (weatherWithDetails.isNotEmpty()) {
                return WeatherMapper().transform(weatherWithDetails)
            } else {
                throw getException()
            }

        } catch (exception: Throwable) {
            throw getException()
        }
    }

    private fun getException(): BusinessException {
        val businessException = BusinessException()
        businessException.businessCode = BusinessException.DEFAULT_DB_ERROR_CODE
        businessException.businessMessage = BusinessException.DEFAULT_DB_ERROR_MESSAGE
        return businessException
    }

    suspend fun saveWeather(
        weatherInfo: WeatherInfo,
        request: WeatherRequest
    ) {
        val weatherEntity = WeatherMapper().revertWeather(weatherInfo)
        weatherEntity.searchKey = request.city
        dao.saveWeather(weatherEntity)
        val weatherItems = WeatherMapper().revertWeatherItem(weatherInfo.foreCastItems)
        weatherItems.forEach { item ->
            item.id = item.date.toInt()
            item.parentId = weatherInfo.id
            dao.saveWeatherItem(item)
        }

    }
}

class WeatherMapper : BaseInfoMapper<WeatherWithDetail, WeatherInfo> {
    override fun transform(input: List<WeatherWithDetail>): WeatherInfo {
        val weatherInfo = WeatherInfo()
        if (input.isNotEmpty()) {
            weatherInfo.id = input[0].weather.id
            weatherInfo.timeZone = input[0].weather.timeZone
            weatherInfo.long = input[0].weather.long
            weatherInfo.lat = input[0].weather.lat
            weatherInfo.county = input[0].weather.county
            weatherInfo.cityName = input[0].weather.cityName
            weatherInfo.foreCastItems = getWeatherItem(input[0].items)
        }
        return weatherInfo
    }

    private fun getWeatherItem(items: List<WeatherItemEntity>): List<WeatherItemInfo>? {
        val outList = mutableListOf<WeatherItemInfo>()
        items.forEach { inItem ->
            val outItem = WeatherItemInfo()
            outItem.id = inItem.id
            outItem.description = inItem.description
            outItem.temperature = inItem.temperature
            outItem.date = inItem.date
            outItem.humanity = inItem.humanity
            outItem.pressure = inItem.pressure
            outList.add(outItem)
        }
        return outList
    }

    fun revertWeather(input: WeatherInfo): WeatherEntity {
        val weatherEntity = WeatherEntity()

        weatherEntity.id = input.id
        weatherEntity.lat = input.lat
        weatherEntity.long = input.long
        weatherEntity.cityName = input.cityName
        weatherEntity.county = input.county
        weatherEntity.timeZone = input.timeZone

        return weatherEntity
    }

    fun revertWeatherItem(items: List<WeatherItemInfo>?): List<WeatherItemEntity> {
        val outList = mutableListOf<WeatherItemEntity>()
        items?.forEach { inItem ->
            val outItem = WeatherItemEntity()
            outItem.id = inItem.id
            outItem.description = inItem.description
            outItem.temperature = inItem.temperature
            outItem.date = inItem.date
            outItem.humanity = inItem.humanity
            outItem.pressure = inItem.pressure
            outList.add(outItem)
        }
        return outList
    }

}
