package com.architecture.cleanmvvm.core.di

import com.architecture.cleanmvvm.weather.di.featureWeather

val appComponent = listOf(
    commonModule,
    repositoryModule,
    featureWeather
)