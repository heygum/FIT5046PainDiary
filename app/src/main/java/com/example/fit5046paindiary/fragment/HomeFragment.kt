package com.example.fit5046paindiary.fragment

import android.content.Context
import android.icu.math.BigDecimal
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fit5046paindiary.RetrofitClient
import com.example.fit5046paindiary.WeatherResponse
import com.example.fit5046paindiary.WeatherService
import com.example.fit5046paindiary.databinding.HomeFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment:Fragment() {
    private var _binding: HomeFragmentBinding? = null
    private val binding get()  = _binding!!

    //the lontitude and latitude of my location
    private var lon: Double = 145.0383139
    private var lat: Double = -37.8848052
    private var appid = "d369f9c4e85439efd98139163b5102fb"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater,container,false)
        val view = binding.root
        val pres = activity?.getPreferences(Context.MODE_PRIVATE)

        val retrofitInterface: WeatherService =
            RetrofitClient.getRetrofitService()

        //call the API of weather
        val callAsync: Call<WeatherResponse> =
            retrofitInterface.getWeatherResponse(
                appid,
                lon,
                lat
            )
        callAsync.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>,
                                    response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val editor = pres?.edit()
                    val temp = BigDecimal(((response.body()!!.main?.temp?.toDouble())?.minus(273.15)!!)).
                    setScale(2, BigDecimal.ROUND_HALF_UP).toString()
                    val humi = response.body()!!.main?.humi
                    val city = response.body()!!.name
                    val pre = response.body()!!.main?.pre
                    editor?.putString("temp",temp)
                    editor?.putString("humi",humi)
                    editor?.putString("pre",pre)
                    editor?.apply()
                    binding.showTemp.setText(temp)
                    binding.showHumi.setText(humi)
                    binding.showCity.setText(city)
                    binding.showPre.setText(pre)
                } else {
                    Log.i("Error ", "Response failed")
                }
            }
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable)
            {
                t.printStackTrace()
            }

        })

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}