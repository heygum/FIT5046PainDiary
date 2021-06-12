package com.example.fit5046paindiary

import com.google.gson.annotations.SerializedName

class WeatherResponse {
     @SerializedName("main")
     var main: Main? = null
     @SerializedName("name")
     var name: String = ""
}

class Main{
     @SerializedName("temp")
     var temp: String = ""
     @SerializedName("pressure")
     var pre: String = ""
     @SerializedName("humidity")
     var humi: String = ""
}