package com.example.fit5046paindiary.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.fit5046paindiary.entity.Pain
import com.example.fit5046paindiary.repository.PainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class RecordViewModel(application: Application):AndroidViewModel(application) {
    private val pRepository :PainRepository

    init {
        pRepository = PainRepository(application)
    }

    var allPain:  LiveData<List<Pain>> = pRepository.allPain.asLiveData()
    var UserPain: LiveData<List<Pain>> = pRepository.UserPain.asLiveData()
    /*var locationCount: LiveData<List<Int>> = pRepository.getLocation()*/

    fun insert(pain: Pain) = viewModelScope.launch(Dispatchers.IO) {
        pRepository.insert(pain)
    }

    fun delete(pain: Pain) = viewModelScope.launch(Dispatchers.IO) {
        pRepository.delete(pain)
    }

    fun update(pain: Pain) = viewModelScope.launch(Dispatchers.IO) {
        pRepository.update(pain)
    }

    suspend fun check(date:String, email:String): Pain? {
        return viewModelScope.async(Dispatchers.IO) {
            pRepository.check(date,email)
        }.await()
    }

    fun getPain(date: String,email: String):Pain{
        return pRepository.getPain(date,email)
    }

    /*fun getLocation(locationList: ArrayList<Int>){
        locationCount = pRepository.getLocation()
    }*/


}