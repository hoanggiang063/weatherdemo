package com.architecture.cleanmvvm.node1.demo.info

import com.architecture.business.core.info.Undefine

data class WeatherInfo(

    var id: String = Undefine.UNDEFINE_STRING,
    var cityName: String = Undefine.UNDEFINE_STRING,
    var lat: Double = Undefine.UNDEFINE_DOUBLE,
    var long: Double = Undefine.UNDEFINE_DOUBLE,
    var county: String = Undefine.UNDEFINE_STRING,
    var timeZone: String? = Undefine.UNDEFINE_STRING,
    var foreCastItems: List<WeatherItemInfo>? = null
)

data class WeatherItemInfo(
    var id: Int = Undefine.UNDEFINE_INT,
    var date: Long = Undefine.UNDEFINE_LONG,
    var temperature: Double = Undefine.UNDEFINE_DOUBLE,
    var pressure: Int = Undefine.UNDEFINE_INT,
    var humanity: Int = Undefine.UNDEFINE_INT,
    var description: String = Undefine.UNDEFINE_STRING
)