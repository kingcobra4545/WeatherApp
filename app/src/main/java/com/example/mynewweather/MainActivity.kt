package com.example.mynewweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.mynewweather.databinding.ActivityMainBinding
import com.example.mynewweather.models.WeatherResponse
import com.example.mynewweather.viewmodels.HomeScreenVM
import com.example.mynewweather.workmanagers.BackgroundSyncManager
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var location : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        location = Constants.CURRENT_LOCATION
        binding.location.text = location
        binding.sunMoon.tag = R.mipmap.sun_icon_foreground
        binding.sunMoon.setOnClickListener {
            Log.i("WM","Am clicked")
            if(binding.sunMoon.tag == R.mipmap.sun_icon_foreground) {
                binding.sunMoon.tag = R.drawable.moon_svg
                binding.sunMoon.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.moon_svg,
                        null
                    )
                )
            }
            else {
                binding.sunMoon.tag = R.mipmap.sun_icon_foreground
                binding.sunMoon.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.mipmap.sun_icon_foreground,
                        null
                    )
                )
            }
        }
        val viewModel = ViewModelProvider(this)[HomeScreenVM::class.java]
        viewModel.currentWeatherLV.observe(this){
            Log.i("Weather","im data here-->${Gson().toJson(it)}")
            updateUI(it)
        }
        viewModel.getCurrentWeather(location)

        startBGWork()

    }

    private fun startBGWork() {
    val backgroundSyncWorkRequest: PeriodicWorkRequest =
        PeriodicWorkRequest.Builder(BackgroundSyncManager::class.java,15, TimeUnit.MINUTES)
            .setInputData(Data.Builder().putString("work_param","work_params").build())
            .build()

        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(backgroundSyncWorkRequest)
        workManager.getWorkInfoByIdLiveData(backgroundSyncWorkRequest.id).observe(this) {
            Log.i("WM", "Data is here from WM --> ${it.outputData.getString("output")}")
        }
    }

    private fun updateUI(it: WeatherResponse?) {
        Log.i("Weather","temp-->${it?.current?.tempC.toString() + "°C"}")
        Log.i("Weather","temp 1-->${it?.current?.tempC}")
        binding.temperature.text = it?.current?.tempC.toString() + "°C"
        binding.feelTemperature.text = it?.current?.feelslikeC.toString() + "°C"
        binding.windDirectionSpeed.text = it?.current?.windKph.toString() + " " + it?.current?.windDir
        binding.updatedLast.text = it?.current?.lastUpdated
    }
}