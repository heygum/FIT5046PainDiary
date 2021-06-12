package com.example.fit5046paindiary.Worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class DbWorker(context: Context,para: WorkerParameters): Worker(context,para) {
    override fun doWork(): Result {
        Log.d("DbWorker","work is doing.")
        return Result.success()
    }
}