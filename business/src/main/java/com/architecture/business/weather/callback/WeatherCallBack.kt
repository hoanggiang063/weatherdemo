package com.architecture.cleanmvvm.node1.demo.callback

import com.architecture.business.core.callback.BasePresentCallBack
import com.architecture.business.core.exception.BusinessException

interface WeatherCallBack<WeatherInfo> : BasePresentCallBack<WeatherInfo> {
    fun onCityNotFound(exception: BusinessException)
}