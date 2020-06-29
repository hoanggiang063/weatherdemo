package com.architecture.cleanmvvm.node1.demo.usecase

import com.architecture.business.core.info.Undefine
import com.architecture.business.core.usecase.BaseUseCase
import com.architecture.cleanmvvm.node1.demo.callback.WeatherCallBack
import com.architecture.cleanmvvm.node1.demo.info.WeatherInfo

interface WeatherUseCase : BaseUseCase<WeatherRequest, WeatherInfo, WeatherCallBack<WeatherInfo>>

data class WeatherRequest(
    var city: String = Undefine.UNDEFINE_STRING,
    var numberDays: Int = Undefine.UNDEFINE_INT,
    var appId: String = Undefine.UNDEFINE_STRING,
    var unit: String = Undefine.DEFAULT_TEMP_UNIT
)