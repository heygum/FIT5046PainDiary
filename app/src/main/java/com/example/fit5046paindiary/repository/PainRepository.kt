package com.example.fit5046paindiary.repository

import android.app.Application
import android.content.res.Resources
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fit5046paindiary.Database.AppDatabase
import com.example.fit5046paindiary.R
import com.example.fit5046paindiary.entity.Pain
import com.example.fit5046paindiary.roomDao.PainDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.collections.ArrayList

class PainRepository(private val application: Application) {
    var painDao:PainDao
    val auth = FirebaseAuth.getInstance()

    init {
        val db: AppDatabase = AppDatabase.getDatabase(application)
        painDao = db.PainDao()
    }

    var email = auth.currentUser.email

    val allPain: Flow<List<Pain>> = painDao.loadAllPain()
    val UserPain: Flow<List<Pain>> = painDao.loadUserPain(email)

    @WorkerThread
    suspend fun insert(pain:Pain)
    {
        painDao.insertPain(pain)
    }

    @WorkerThread
    suspend fun delete(pain: Pain){
        painDao.deletePain(pain)
    }

    @WorkerThread
    suspend fun update(pain: Pain){
        painDao.updatePain(pain)
    }

    @WorkerThread
    suspend fun check(date:String, email:String):Pain? {
        return painDao.check(date,email)
    }

    fun getPain(date: String,email: String):Pain{
        return painDao.getPain(date,email)
    }

    /*fun getLocation():LiveData<List<Int>>
    {
        val locationList = application.resources.getStringArray(R.array.pain_location_array)
        val list: ArrayList<Int> = ArrayList()
        for(i in 0..locationList.size) {
            val locationCount = painDao.locationCount(email, locationList[i])
            list.add(locationCount)
        }
        return MutableLiveData(list)
    }*/

}