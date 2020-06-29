package com.architecture.repository.core.mapper

import com.architecture.repository.weather.local.model.WeatherWithDetail

interface BaseInfoMapper<In, Out> {
    fun transform(input: List<WeatherWithDetail>): Out
}