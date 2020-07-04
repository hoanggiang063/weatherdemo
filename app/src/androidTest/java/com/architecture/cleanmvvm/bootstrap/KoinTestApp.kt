package com.architecture.cleanmvvm.bootstrap

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class KoinTestApp: Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidLogger()
      androidContext(this@KoinTestApp)
      modules(emptyList())
    }
  }

  internal fun injectModule(module: Module) {
    loadKoinModules(module)
  }
}