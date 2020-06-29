package com.architecture.cleanmvvm

import android.app.Application
import com.architecture.cleanmvvm.core.di.appComponent
import com.architecture.cleanmvvm.core.security.SecurityMonitor
import org.koin.android.ext.android.startKoin

open class CleanApp : Application() {

    override fun onCreate() {
        super.onCreate()
        configureDi()
    }

    // CONFIGURATION ---
    open fun configureDi() =
        startKoin(this, provideComponent())

    // PUBLIC API ---
    open fun provideComponent() = appComponent
}