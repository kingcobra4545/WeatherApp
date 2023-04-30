package com.example.mynewweather.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynewweather.Constants
import com.example.mynewweather.models.WeatherResponse
import com.example.mynewweather.network.RetrofitInstance
import com.example.mynewweather.network.RetrofitInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenVM: ViewModel() {
    private val api = RetrofitInstance.retrofitInstance.create(RetrofitInterface::class.java)
    private val currentWeatherMLV : MutableLiveData<WeatherResponse> = MutableLiveData()
    val currentWeatherLV : LiveData<WeatherResponse>
        get() = currentWeatherMLV
    fun getCurrentWeather(location: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val currentWeatherRequest = api.getCurrentWeather(Constants.api_key,location)
            if(currentWeatherRequest.isSuccessful){
                currentWeatherMLV.postValue(currentWeatherRequest.body())
            }
            else
                currentWeatherMLV.postValue(null)
        }

    }
}