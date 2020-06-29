package com.architecture.repository.weather.remote.model

data class WeatherModel(
    var city: City,
    var list: List<ForeCast>
)

data class City(
    var id: String,
    var name: String,
    var coord: CityCoord,
    var country: String,
    var population: Int = 0,
    var timeZone: String
)

data class CityCoord(
    var lon: Double = 0.0,
    var lat: Double = 0.0
)

data class ForeCast(
    var dt: Long = 0,
    var sunrise: Double = 0.0,
    var sunset: Double = 0.0,
    var temp: Temperature,
    var feels_like: FeelsLike,
    var pressure: Int = 0,
    var humidity: Int = 0,
    var weather: List<Weather>,
    var speed: Double = 0.0,
    var deg: Double = 0.0,
    var clouds: Double = 0.0,
    var rain: Double = 0.0
)


data class Temperature(
    var day: Double = 0.0,
    var min: Double = 0.0,
    var max: Double = 0.0,
    var night: Double = 0.0,
    var eve: Double = 0.0,
    var morn: Double = 0.0
)

data class FeelsLike(
    var day: Double = 0.0,
    var night: Double = 0.0,
    var eve: Double = 0.0,
    var morn: Double = 0.0
)

data class Weather(
    var id: String,
    var main: String,
    var description: String,
    var icon: String
)
