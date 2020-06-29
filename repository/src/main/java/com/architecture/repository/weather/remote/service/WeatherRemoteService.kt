package com.architecture.repository.weather.remote.service

import com.architecture.repository.weather.remote.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherRemoteService {
    @GET("/data/2.5/forecast/daily")
    suspend fun getWeather(
        @Query(value = "q") city: String,
        @Query(value = "cnt") numberOfDate: String,
        @Query(value = "appid") appId: String,
        @Query(value = "units") unit: String
    ): WeatherModel
}