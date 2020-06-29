package com.architecture.repository.weather.local.service

import androidx.room.*
import com.architecture.repository.weather.local.model.WeatherEntity
import com.architecture.repository.weather.local.model.WeatherItemEntity
import com.architecture.repository.weather.local.model.WeatherWithDetail

@Dao
abstract class WeatherDao() {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertWeather(weatherEntity: WeatherEntity)

    @Transaction
    @Query("SELECT * FROM WeatherEntity WHERE cityName LIKE '%' || :searchName || '%' OR searchKey LIKE '%' || :searchName || '%'")
    abstract suspend fun getWeatherWithFullDetail(searchName: String): List<WeatherWithDetail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertItem(weatherEntity: WeatherItemEntity)

    suspend fun saveWeather(weatherEntity: WeatherEntity) {
        insertWeather(weatherEntity)
    }

    suspend fun saveWeatherItem(weatherItemEntity: WeatherItemEntity) {
        insertItem(weatherItemEntity)
    }

}