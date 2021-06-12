package com.example.fit5046paindiary

import retrofit2.http.GET
import retrofit2.http.Path
import com.example.fit5046paindiary.WeatherResponse
import retrofit2.Call
import retrofit2.http.Query

//http://api.openweathermap.org/data/2.5/weather?appid=d369f9c4e85439efd98139163b5102fb&lat=-37.8848052&lon=145.0383139

interface WeatherService {
    @GET("weather")
    fun getWeatherResponse(
        @Query("appid") appid: String,
        @Query("lon") lon: Double,
        @Query("lat") lat: Double
    ):Call<WeatherResponse>
}