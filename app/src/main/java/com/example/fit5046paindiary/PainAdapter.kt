package com.example.fit5046paindiary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.fit5046paindiary.entity.Pain

class PainAdapter(var PainList: List<Pain>):RecyclerView.Adapter<PainAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val level: TextView = view.findViewById(R.id.painLevel)
        val location: TextView = view.findViewById(R.id.painLocation)
        val mood: TextView = view.findViewById(R.id.painMood)
        val steps: TextView = view.findViewById(R.id.painSteps)
        val date: TextView = view.findViewById(R.id.painDate)
        val temp: TextView = view.findViewById(R.id.painTemp)
        val humi: TextView = view.findViewById(R.id.painHumi)
        val pre:  TextView = view.findViewById(R.id.painPre)
        val email: TextView = view.findViewById(R.id.painEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pain_item,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pain = PainList[position]
        holder.level.text = "Pain Level: " +pain.level.toString()
        holder.location.text = "Pain Location: " + pain.location
        holder.mood.text = "Mood: " + intToMood(pain.mood)
        holder.steps.text = "Step today: " + pain.step.toString()
        holder.temp.text = "Temperature: " + pain.temp.toString()
        holder.humi.text = "Humidity: " + pain.humi.toString()
        holder.pre.text = "Pressure: " + pain.press.toString()
        holder.date.text = "Date: " + pain.date
        holder.email.text = "Email: "+ pain.email

    }

    fun intToMood(int:Int) = when(int){
        1 -> "Very good"
        2 -> "Good"
        3 -> "Average"
        4 -> "Bad"
        5 -> "Very Bad"
        else -> "?"
    }

    override fun getItemCount() = PainList.size
}