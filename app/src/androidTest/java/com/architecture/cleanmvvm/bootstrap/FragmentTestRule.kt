package com.architecture.cleanmvvm.bootstrap

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.architecture.cleanmvvm.R
import com.architecture.cleanmvvm.core.view.MainActivity
import org.koin.core.module.Module

abstract class FragmentTestRule<F : Fragment> :
    ActivityTestRule<MainActivity>(MainActivity::class.java, true) {
    override fun afterActivityLaunched() {
        super.afterActivityLaunched()

        activity.runOnUiThread {
            val supportFragmentManager = activity.supportFragmentManager
            val fragmentTransaction: FragmentTransaction =
                supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, createFragment(), null)
            fragmentTransaction.commit()
        }
    }

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        val application =
            InstrumentationRegistry.getInstrumentation().targetContext
                .applicationContext as KoinTestApp
        application.injectModule(getModule())
    }

    protected abstract fun createFragment(): F

    protected abstract fun getModule(): Module

    fun launch() {
        launchActivity(Intent())
    }
}

fun <F : Fragment> createRule(fragment: F, module: Module): FragmentTestRule<F> =
    object : FragmentTestRule<F>() {
        override fun createFragment(): F = fragment
        override fun getModule(): Module = module
    }