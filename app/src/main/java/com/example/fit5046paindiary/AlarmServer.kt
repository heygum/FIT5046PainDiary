package com.example.fit5046paindiary

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class AlarmServer: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("Alarm","Have set the alarm.")
        Toast.makeText(context,"It is time to enter your daily data.", Toast.LENGTH_LONG).show()
    }
}