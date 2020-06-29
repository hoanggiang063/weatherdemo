package com.architecture.cleanmvvm.weather.di

import com.architecture.cleanmvvm.node1.demo.repository.WeatherRepository
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherUseCase
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherUseCaseImpl
import com.architecture.cleanmvvm.weather.viewmodel.WeatherViewModel
import com.architecture.repository.weather.cache.repository.WeatherCacheImpl
import com.architecture.repository.weather.local.repository.WeatherLocalImpl
import com.architecture.repository.weather.remote.repository.WeatherRemoteImpl
import com.architecture.repository.weather.remote.service.WeatherRemoteService
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit

val featureWeather = module {

    factory {
        get<Retrofit>().create(WeatherRemoteService::class.java) as WeatherRemoteService
    }

    factory {
        WeatherRemoteImpl(get())
    }

    factory {
        WeatherLocalImpl(get())
    }

    factory {
        WeatherCacheImpl(
            false,
            get(),
            get()
        ) as WeatherRepository
    }

    factory {
        WeatherUseCaseImpl(get()) as WeatherUseCase
    }

    viewModel { WeatherViewModel(get(), get()) }
}