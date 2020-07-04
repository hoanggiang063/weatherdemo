package com.architecture.cleanmvvm

import android.app.Application
import com.architecture.cleanmvvm.core.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class CleanApp : Application() {

    override fun onCreate() {
        super.onCreate()
        configureDi()
    }

    // CONFIGURATION ---
    open fun configureDi() =
        //startKoin(this, provideComponent())
        startKoin {
            androidLogger()
            androidContext(this@CleanApp)
            modules(provideComponent())
        }

    // PUBLIC API ---
    open fun provideComponent() = appComponent
}