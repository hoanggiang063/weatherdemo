package com.architecture.repository.weather.remote.repository

import com.architecture.cleanmvvm.node1.demo.info.WeatherInfo
import com.architecture.cleanmvvm.node1.demo.info.WeatherItemInfo
import com.architecture.cleanmvvm.node1.demo.repository.WeatherRepository
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherRequest
import com.architecture.repository.core.mapper.BaseExceptionMapperImpl
import com.architecture.repository.weather.remote.model.ForeCast
import com.architecture.repository.weather.remote.model.Weather
import com.architecture.repository.weather.remote.model.WeatherModel
import com.architecture.repository.weather.remote.service.WeatherRemoteService

class WeatherRemoteImpl(var service: WeatherRemoteService) : WeatherRepository {

    lateinit var request: WeatherRequest

    override fun setParam(param: WeatherRequest) {
        request = param
    }

    override suspend fun invoke(): WeatherInfo {
        try {
            return RemoteMapper().transform(
                service.getWeather(
                    request.city,
                    request.numberDays.toString(),
                    request.appId,
                    request.unit
                )
            )
        } catch (error: Throwable) {
            throw BaseExceptionMapperImpl().transform(error)
        }
    }
}

class RemoteMapper {
    fun transform(input: WeatherModel): WeatherInfo {
        val weatherInfo = WeatherInfo()
        weatherInfo.cityName = input.city.name
        weatherInfo.county = input.city.country
        weatherInfo.id = input.city.id
        weatherInfo.lat = input.city.coord.lat
        weatherInfo.long = input.city.coord.lon
        weatherInfo.timeZone = input.city.timeZone
        input.list.let {
            weatherInfo.foreCastItems = transformItem(input.list)
        }
        return weatherInfo
    }

    private fun transformItem(list: List<ForeCast>): List<WeatherItemInfo> {
        val itemInfo = mutableListOf<WeatherItemInfo>()
        list.forEach { inItem ->
            val outItem = WeatherItemInfo()
            outItem.pressure = inItem.pressure
            outItem.temperature = inItem.temp.eve
            outItem.description = getItemDescription(inItem.weather)
            outItem.humanity = inItem.humidity
            outItem.date = inItem.dt
            itemInfo.add(outItem)
        }
        return itemInfo
    }

    private fun getItemDescription(weather: List<Weather>): String {
        var totalDescription = ""
        weather.forEach { item ->
            totalDescription += item.description
        }
        return totalDescription
    }

}
