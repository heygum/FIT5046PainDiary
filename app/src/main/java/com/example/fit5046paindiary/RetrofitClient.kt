package com.example.fit5046paindiary

import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object RetrofitManager {
        fun getRetrofitService(): WeatherService {
            val retrofit = Retrofit.Builder()
                .baseUrl(HttpUrl.parse("https://api.openweathermap.org/data/2.5/")!!)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(WeatherService::class.java)
        }
    }
}