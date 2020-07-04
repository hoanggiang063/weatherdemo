package com.architecture.cleanmvvm.weather.view

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.architecture.cleanmvvm.R
import com.architecture.cleanmvvm.bootstrap.createRule
import com.architecture.cleanmvvm.node1.demo.info.WeatherInfo
import com.architecture.cleanmvvm.weather.viewmodel.WeatherViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module

// Run the test with emulator > Android P
private const val SEARCH_TEXT = "melbourne"

@RunWith(AndroidJUnit4ClassRunner::class)
class WeatherFragmentTest {

    private val fragmentViewModel = mockViewModel()
    private val fragment = WeatherFragment()

    @get:Rule
    val fragmentRule = createRule(fragment, module {
        single(override = true) {
            fragmentViewModel
        }
    })

    private fun mockViewModel(): WeatherViewModel {

        val viewModel = mockk<WeatherViewModel>(relaxed = true)
        val userData = MutableLiveData<WeatherInfo>()
        val failedByTechnical = MutableLiveData<Throwable>()
        val failedException = MutableLiveData<Throwable>()
        val failedByBusiness = MutableLiveData<Throwable>()

        every { viewModel.currentWeatherInfo } returns userData
        every { viewModel.failedByTechnical } returns failedByTechnical
        every { viewModel.failedException } returns failedException
        every { viewModel.failedByBusiness } returns failedByBusiness
        return viewModel
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun shouldSearchWhenInputMoreThan3Letters() {
        onView(withId(R.id.viewSearch)).perform(typeText(SEARCH_TEXT), closeSoftKeyboard())
        onView(withId(R.id.btnGetWeather)).perform(click())
        verify {
            fragmentViewModel.loadWeather(SEARCH_TEXT)
        }

    }

    @Test
    fun shouldNotSearchWhenInputMoreThan3Letters() {
        val invalidSearchText = "hc"
        onView(withId(R.id.viewSearch)).perform(typeText(invalidSearchText), closeSoftKeyboard())
        onView(withId(R.id.btnGetWeather)).perform(click())
        verify(exactly = 0) {
            fragmentViewModel.loadWeather(any())
        }

    }

    @Test
    fun shouldNotSearchMoreThanOneTimeWhenUserClickManyTime() {
        onView(withId(R.id.viewSearch)).perform(typeText(SEARCH_TEXT), closeSoftKeyboard())
        onView(withId(R.id.btnGetWeather)).perform(click(), click(), click())
        verify(exactly = 1) {
            fragmentViewModel.loadWeather(any())
        }
    }

    //Should have a testcases when having data, fail by technical, default fail and fail by business
}
