package com.architecture.cleanmvvm.core.di

import com.architecture.cleanmvvm.core.configuration.ConfigConnector
import com.architecture.cleanmvvm.core.configuration.EnvConfiguration
import com.architecture.cleanmvvm.core.security.SecurityMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val commonModule = module {
    single {
        SecurityMonitor(androidContext())
    }

    single {
       ConfigConnector().envConfiguration as EnvConfiguration
    }
}