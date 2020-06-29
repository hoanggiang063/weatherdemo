package com.architecture.cleanmvvm.core.di

import com.architecture.cleanmvvm.BuildConfig
import com.architecture.cleanmvvm.core.configuration.EnvConfiguration
import com.architecture.repository.weather.local.service.WeatherDatabase
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val DATABASE = "DATABASE"
private const val DEBUG_HTTP = "DebugHttp"
private const val RELEASE_HTTP = "ReleaseHttp"

val repositoryModule = module {

    single(DEBUG_HTTP) {
        val debugInterceptor = HttpLoggingInterceptor()
        debugInterceptor.level = HttpLoggingInterceptor.Level.BODY
        debugInterceptor as Interceptor
    }

    single(RELEASE_HTTP) {
        val releaseInterceptor = HttpLoggingInterceptor()
        releaseInterceptor.level = HttpLoggingInterceptor.Level.NONE
        releaseInterceptor as Interceptor
    }

    single {

        val config: EnvConfiguration = get()
        val certificatePinner = CertificatePinner.Builder()
            .add(config.getEnvironmentUrl(), config.getSpinningKey())
            .build()

        var interceptor:Interceptor = get(DEBUG_HTTP)
        if(!BuildConfig.DEBUG) interceptor = get(RELEASE_HTTP)

        OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .addInterceptor(
                interceptor
        ).build() as OkHttpClient
    }

    single {
        val config: EnvConfiguration = get()
        Retrofit.Builder()
            .baseUrl(config.getEnvironmentUrl())
            .client(
                get()
            )
            .addConverterFactory(MoshiConverterFactory.create())
            .build() as Retrofit
    }

    single(DATABASE) {
        val config: EnvConfiguration = get()
        WeatherDatabase.buildDatabase(androidContext(), config.getEnvironmentApiKey())
    }
    factory {
        (get(DATABASE) as WeatherDatabase).weatherDao()
    }


}