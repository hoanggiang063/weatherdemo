package com.architecture.cleanmvvm.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.architecture.business.core.exception.BusinessException
import com.architecture.business.core.exception.TechnicalException
import com.architecture.cleanmvvm.core.configuration.EnvConfiguration
import com.architecture.cleanmvvm.node1.demo.callback.WeatherCallBack
import com.architecture.cleanmvvm.node1.demo.info.WeatherInfo
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherRequest
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherUseCase
import okhttp3.CertificatePinner





class WeatherViewModel(val weatherUseCase: WeatherUseCase, val envConfiguration: EnvConfiguration) : ViewModel() {

    private val _currentWeatherInfo = MutableLiveData<WeatherInfo>()
    var currentWeatherInfo: LiveData<WeatherInfo> = _currentWeatherInfo

    private val _failedException = MutableLiveData<Throwable>()
    var failedException: LiveData<Throwable> = _failedException

    private val _failedByBusiness = MutableLiveData<Throwable>()
    var failedByBusiness: LiveData<Throwable> = _failedByBusiness

    private val _failedByTechnical = MutableLiveData<Throwable>()
    var failedByTechnical: LiveData<Throwable> = _failedByTechnical

    fun loadWeather(searchText: String) {
        val request = WeatherRequest()
        request.city = searchText
        request.appId = envConfiguration.getEnvironmentApiKey()
        request.unit = envConfiguration.getEnvironmentUnit()
        request.numberDays = 7
        weatherUseCase
            .buildUseCase(request)
            .invoke(object : WeatherCallBack<WeatherInfo> {
                override fun onCityNotFound(exception: BusinessException) {
                    _failedByBusiness.value = exception
                }

                override fun onSuccess(expectedResult: WeatherInfo?) {
                    _currentWeatherInfo.value = expectedResult
                }

                override fun onFailByTechnical(exception: TechnicalException) {
                    _failedByTechnical.value = exception
                }

                override fun onDefaultFail(throwable: Throwable) {
                    _failedException.value = throwable
                }

            })
    }
}