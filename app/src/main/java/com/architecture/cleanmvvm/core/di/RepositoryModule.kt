package com.architecture.cleanmvvm.core.di

import com.architecture.cleanmvvm.BuildConfig
import com.architecture.cleanmvvm.core.configuration.EnvConfiguration
import com.architecture.repository.weather.local.service.WeatherDatabase
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val DATABASE = "DATABASE"
private const val DEBUG_HTTP = "DebugHttp"
private const val RELEASE_HTTP = "ReleaseHttp"

val repositoryModule = module {

    single<Interceptor>(qualifier = named(DEBUG_HTTP)) {
        val debugInterceptor = HttpLoggingInterceptor()
        debugInterceptor.level = HttpLoggingInterceptor.Level.BODY
        debugInterceptor
    }

    single<Interceptor>(qualifier = named(RELEASE_HTTP)) {
        val releaseInterceptor = HttpLoggingInterceptor()
        releaseInterceptor.level = HttpLoggingInterceptor.Level.NONE
        releaseInterceptor
    }

    single<OkHttpClient> {

        val config: EnvConfiguration = get()
        val certificatePinner = CertificatePinner.Builder()
            .add(config.getEnvironmentUrl(), config.getSpinningKey())
            .build()

        var interceptor: Interceptor = get(qualifier = named(DEBUG_HTTP))
        if (!BuildConfig.DEBUG) interceptor = get(qualifier = named(RELEASE_HTTP))

        OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .addInterceptor(
                interceptor
            ).build()
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

    single(qualifier = named(DATABASE)) {
        val config: EnvConfiguration = get()
        WeatherDatabase.buildDatabase(androidContext(), config.getEnvironmentApiKey())
    }
    factory {
        (get(qualifier = named(DATABASE)) as WeatherDatabase).weatherDao()
    }


}