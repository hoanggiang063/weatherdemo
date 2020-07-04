package com.architecture.cleanmvvm.weather.view

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.architecture.business.core.exception.BusinessException
import com.architecture.business.core.exception.TechnicalException
import com.architecture.business.core.exception.UnknownException
import com.architecture.cleanmvvm.R
import com.architecture.cleanmvvm.bootstrap.createRule
import com.architecture.cleanmvvm.node1.demo.info.WeatherInfo
import com.architecture.cleanmvvm.node1.demo.info.WeatherItemInfo
import com.architecture.cleanmvvm.weather.util.ToastMatcher
import com.architecture.cleanmvvm.weather.viewmodel.WeatherViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module

// Run the test with emulator > Android P
@RunWith(AndroidJUnit4ClassRunner::class)
class WeatherFragmentTest {
    companion object {
        private const val SEARCH_TEXT = "melbourne"
        private const val SEARCH_TEXT_FAIL_DEFAULT = "fail default"
        private const val SEARCH_TEXT_FAIL_TECHNICAL = "fail technical"
        private const val SEARCH_TEXT_FAIL_BUSINESS = "fail business"
        private const val SEARCH_TEXT_SUCCESS = "dalat"
    }

    private val fragmentViewModel = mockViewModel()
    private val fragment = WeatherFragment()
    private lateinit var userData: MutableLiveData<WeatherInfo>
    private lateinit var failedByTechnical: MutableLiveData<Throwable>
    private lateinit var failedException: MutableLiveData<Throwable>
    private lateinit var failedByBusiness: MutableLiveData<Throwable>

    @get:Rule
    val fragmentRule = createRule(fragment, module {
        single(override = true) {
            fragmentViewModel
        }
    })

    private fun mockViewModel(): WeatherViewModel {
        userData = MutableLiveData<WeatherInfo>()
        failedByTechnical = MutableLiveData<Throwable>()
        failedException = MutableLiveData<Throwable>()
        failedByBusiness = MutableLiveData<Throwable>()
        val viewModel = mockk<WeatherViewModel>(relaxed = true)

        every { viewModel.currentWeatherInfo } returns userData
        every { viewModel.failedByTechnical } returns failedByTechnical
        every { viewModel.failedException } returns failedException
        every { viewModel.failedByBusiness } returns failedByBusiness

        every { viewModel.loadWeather(SEARCH_TEXT_FAIL_DEFAULT) }.answers {
            failedException.value = UnknownException()
        }

        every { viewModel.loadWeather(SEARCH_TEXT_FAIL_TECHNICAL) }.answers {
            failedByTechnical.value = TechnicalException()
        }

        every { viewModel.loadWeather(SEARCH_TEXT_FAIL_BUSINESS) }.answers {
            val exception = BusinessException()
            exception.businessCode = BusinessException.DEFAULT_DB_ERROR_CODE
            exception.businessMessage = BusinessException.DEFAULT_DB_ERROR_MESSAGE
            failedByBusiness.value = exception
        }

        every { viewModel.loadWeather(SEARCH_TEXT_SUCCESS) }.answers {
            val weatherInfo = WeatherInfo()
            weatherInfo.foreCastItems = listOf(WeatherItemInfo())
            userData.value = weatherInfo
        }

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

    @Test
    fun shouldShowDefaultFailDialogWhenViewModelGotFailException() {
        onView(withId(R.id.viewSearch)).perform(
            typeText(SEARCH_TEXT_FAIL_DEFAULT),
            closeSoftKeyboard()
        )
        onView(withId(R.id.btnGetWeather)).perform(click())
        onView(withText(R.string.defaultFailMessage)).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowTechnicalFailDialogWhenViewModelGotTechnicalException() {
        onView(withId(R.id.viewSearch)).perform(
            typeText(SEARCH_TEXT_FAIL_TECHNICAL),
            closeSoftKeyboard()
        )
        onView(withId(R.id.btnGetWeather)).perform(click())
        onView(withText(R.string.technicalFailMessage)).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowBusinessDialogWhenViewModelGotBusinessException() {
        onView(withId(R.id.viewSearch)).perform(
            typeText(SEARCH_TEXT_FAIL_BUSINESS),
            closeSoftKeyboard()
        )
        onView(withId(R.id.btnGetWeather)).perform(click())
        val businessMessage = String.format(
            "Fail by load data, Error: %s, reason: %s",
            BusinessException.DEFAULT_DB_ERROR_CODE,
            BusinessException.DEFAULT_DB_ERROR_MESSAGE
        )
        onView(withText(businessMessage)).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowDataWhenViewModelGoSuccessResult() {
        onView(withId(R.id.viewSearch)).perform(typeText(SEARCH_TEXT_SUCCESS), closeSoftKeyboard())
        onView(withId(R.id.btnGetWeather)).perform(click())
        onView(withId(R.id.recyclerForeCast))
            .check(matches(atPosition(0, hasDescendant(withText("Pressure: 0")))))
    }

    private fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View>? {
        checkNotNull(itemMatcher)
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
}
