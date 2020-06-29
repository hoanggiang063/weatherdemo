package com.architecture.cleanmvvm.core.configuration

interface EnvConfiguration {
    fun getEnvironmentUrl(): String
    fun getEnvironmentApiKey(): String
    fun getEnvironmentUnit(): String
    fun getSpinningKey():String
}