package com.example.mynewweather.network

import com.example.mynewweather.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey:String,
        @Query("q") param:String
    ):Response<WeatherResponse>
}