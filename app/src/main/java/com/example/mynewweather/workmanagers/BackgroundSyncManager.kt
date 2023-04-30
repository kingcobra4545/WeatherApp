package com.example.mynewweather.workmanagers

import android.app.NotificationChannel
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.mynewweather.Constants
import com.example.mynewweather.network.RetrofitInstance
import com.example.mynewweather.network.RetrofitInterface
import com.google.gson.Gson

class BackgroundSyncManager(appContext: Context, workerParams:WorkerParameters ) :CoroutineWorker(appContext, workerParams) {
    private val api = RetrofitInstance.retrofitInstance.create(RetrofitInterface::class.java)

    override suspend fun doWork(): Result {
        val response = api.getCurrentWeather(Constants.api_key,Constants.CURRENT_LOCATION).body()
        return Result.success(Data.Builder().putString("output",Gson().toJson(response)).build())
    }
}