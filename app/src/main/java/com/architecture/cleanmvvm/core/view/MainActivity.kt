package com.architecture.cleanmvvm.core.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.architecture.cleanmvvm.BuildConfig
import com.architecture.cleanmvvm.R
import com.architecture.cleanmvvm.core.security.SecurityMonitor
import com.architecture.cleanmvvm.weather.view.WeatherFragment
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    val securityMonitor: SecurityMonitor by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_base)
        if (isLoadMainView()) {
            hostFragment(WeatherFragment())
        } else {
            hostFragment(UnsafeFragment())
        }
    }

    private fun isLoadMainView(): Boolean {
        return (BuildConfig.BUILD_TYPE == "release" && securityMonitor.isSafeEnvironment()) ||
                BuildConfig.BUILD_TYPE == "debug"
    }

    private fun hostFragment(fragment: Fragment) {
        if (supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) == null) {
            val fragmentTransaction: FragmentTransaction =
                supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, fragment, fragment.javaClass.simpleName)
            fragmentTransaction.commit()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val configuration = Configuration(newConfig)
        adjustFontScale(configuration)
    }

    private fun adjustFontScale(configuration: Configuration) {
        val metrics = resources.displayMetrics
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }
}
