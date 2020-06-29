package com.architecture.cleanmvvm.node1.demo.usecase

import com.architecture.business.core.exception.BusinessException
import com.architecture.business.core.usecase.BaseUsecaseImpl
import com.architecture.cleanmvvm.node1.demo.callback.WeatherCallBack
import com.architecture.cleanmvvm.node1.demo.info.WeatherInfo
import com.architecture.cleanmvvm.node1.demo.repository.WeatherRepository

class WeatherUseCaseImpl(weatherRepository: WeatherRepository) :
    WeatherUseCase,
    BaseUsecaseImpl<WeatherRequest, WeatherInfo, WeatherCallBack<WeatherInfo>>(weatherRepository) {

    override fun handleExceptionByChild(
        error: Throwable,
        callback: WeatherCallBack<WeatherInfo>
    ): Boolean {
        if (error is BusinessException) {
            callback.onCityNotFound(error)
            return true
        }
        return false
    }
}