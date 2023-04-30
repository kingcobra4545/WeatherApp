package com.example.mynewweather.network

import com.example.mynewweather.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
companion object{
    var interceptor = Interceptor{
        var request = it.request()
        request = request.newBuilder()
            .addHeader("Authorisation","")
            .build()
        return@Interceptor it.proceed(request)
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    val retrofitInstance:Retrofit
    get() {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    }


}
}