package com.example.fit5046paindiary.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Pain(var level:Int,var location: String, var mood:Int,var step: Int,
var date:String,var temp: Double,var humi: Int,var press:Int,var email:String)
{
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0
}