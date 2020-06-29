package com.architecture.cleanmvvm.weather.view

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.architecture.business.core.exception.BusinessException
import com.architecture.cleanmvvm.R
import com.architecture.cleanmvvm.node1.demo.info.WeatherInfo
import com.architecture.cleanmvvm.weather.viewmodel.WeatherViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {
    companion object {
        const val SEARCH_MIN_CHARACTER_LIMIT = 3
    }

    private val searchView: SearchView by lazy {
        view!!.findViewById<SearchView>(R.id.searchView)
    }

    private val btnGetWeather: AppCompatButton by lazy {
        view!!.findViewById<AppCompatButton>(R.id.btnGetWeather)
    }

    private val recyclerForeCast: RecyclerView by lazy {
        view!!.findViewById<RecyclerView>(R.id.recyclerForeCast)
    }

    private val viewModel: WeatherViewModel by viewModel()

    private lateinit var weatherAdapter: WeatherAdapter

    private val lastClickTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getSetUpView(), container, false)
    }

    private fun getSetUpView(): Int {
        return R.layout.fragment_weather
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpListView()
        listenSuccessData()
        listenFailData()

        btnGetWeather.setOnClickListener {
            doSearch()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                doSearch()
                return false
            }
        })
        super.onViewCreated(view, savedInstanceState)
    }

    private fun doSearch() {
        clearScreenData()
        if (isValidToLoadData()) {
            loadScreenData()
        }
    }

    private fun isValidToLoadData(): Boolean {
        return (searchView.query.length >= SEARCH_MIN_CHARACTER_LIMIT) &&
                (SystemClock.elapsedRealtime() - lastClickTime > 1000)
    }

    private fun setUpListView() {
        weatherAdapter = WeatherAdapter()
        recyclerForeCast.layoutManager = LinearLayoutManager(context)
        recyclerForeCast.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerForeCast.adapter = weatherAdapter
    }

    private fun listenSuccessData() {
        val dataObserver = Observer<WeatherInfo> { data ->
            data.foreCastItems?.let {
                weatherAdapter.info.clear()
                weatherAdapter.info.addAll(data.foreCastItems!!)
                weatherAdapter.notifyDataSetChanged()
            }

        }
        viewModel.currentWeatherInfo.observe(this, dataObserver)
    }

    private fun listenFailData() {
        val failDefaultObserver = Observer<Throwable> { _ ->
            showError(getString(R.string.defaultFailMessage))
            clearScreenData()
        }
        viewModel.failedException.observe(this, failDefaultObserver)

        val failTechnicalObserver = Observer<Throwable> { _ ->
            showError(getString(R.string.technicalFailMessage))
            clearScreenData()
        }
        viewModel.failedByTechnical.observe(this, failTechnicalObserver)

        val failByBusinessObserver = Observer<Throwable> { data ->
            val businessException = data as BusinessException
            val textMessage = getString(R.string.businessFailMessage)
            showError(
                String.format(
                    textMessage,
                    businessException.businessCode,
                    businessException.businessMessage
                )
            )
            clearScreenData()
        }
        viewModel.failedByBusiness.observe(this, failByBusinessObserver)
    }

    private fun loadScreenData() {
        searchView.query?.let { text ->
            viewModel.loadWeather(text.toString())
        }

    }

    private fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    private fun clearScreenData() {
        weatherAdapter.info.clear()
        weatherAdapter.notifyDataSetChanged()
    }

}