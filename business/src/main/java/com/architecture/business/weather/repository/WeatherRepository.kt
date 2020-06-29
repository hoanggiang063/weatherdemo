package com.architecture.cleanmvvm.node1.demo.repository

import com.architecture.business.core.repository.BaseRepository
import com.architecture.cleanmvvm.node1.demo.info.WeatherInfo
import com.architecture.cleanmvvm.node1.demo.usecase.WeatherRequest

interface WeatherRepository : BaseRepository<WeatherRequest, WeatherInfo>