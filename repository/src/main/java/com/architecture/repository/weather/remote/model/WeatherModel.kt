package com.architecture.repository.weather.remote.model

import com.google.gson.annotations.SerializedName

data class WeatherModel(
    @SerializedName("city")
    var city: City,

    @SerializedName("list")
    var list: List<ForeCast>
)

data class City(
    @SerializedName("id")
    var id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("coord")
    var coord: CityCoord,

    @SerializedName("country")
    var country: String,

    @SerializedName("population")
    var population: Int = 0,

    @SerializedName("timeZone")
    var timeZone: String
)

data class CityCoord(
    @SerializedName("lon")
    var lon: Double = 0.0,

    @SerializedName("lat")
    var lat: Double = 0.0
)

data class ForeCast(
    @SerializedName("dt")
    var dt: Long = 0,

    @SerializedName("sunrise")
    var sunrise: Double = 0.0,

    @SerializedName("sunset")
    var sunset: Double = 0.0,

    @SerializedName("temp")
    var temp: Temperature,

    @SerializedName("feels_like")
    var feels_like: FeelsLike,

    @SerializedName("pressure")
    var pressure: Int = 0,

    @SerializedName("humidity")
    var humidity: Int = 0,

    @SerializedName("weather")
    var weather: List<Weather>,

    @SerializedName("speed")
    var speed: Double = 0.0,

    @SerializedName("deg")
    var deg: Double = 0.0,

    @SerializedName("clouds")
    var clouds: Double = 0.0,

    @SerializedName("rain")
    var rain: Double = 0.0
)


data class Temperature(
    @SerializedName("day")
    var day: Double = 0.0,

    @SerializedName("min")
    var min: Double = 0.0,

    @SerializedName("max")
    var max: Double = 0.0,

    @SerializedName("night")
    var night: Double = 0.0,

    @SerializedName("eve")
    var eve: Double = 0.0,

    @SerializedName("morn")
    var morn: Double = 0.0
)

data class FeelsLike(
    @SerializedName("day")
    var day: Double = 0.0,

    @SerializedName("night")
    var night: Double = 0.0,

    @SerializedName("eve")
    var eve: Double = 0.0,

    @SerializedName("morn")
    var morn: Double = 0.0
)

data class Weather(
    @SerializedName("id")
    var id: String,

    @SerializedName("main")
    var main: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("icon")
    var icon: String
)
