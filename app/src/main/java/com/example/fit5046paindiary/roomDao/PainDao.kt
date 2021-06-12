package com.example.fit5046paindiary.roomDao

import android.location.Location
import androidx.room.*
import com.example.fit5046paindiary.entity.Pain
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface PainDao {
    @Insert
    fun insertPain(newPain:Pain):Long

    @Update
    fun updatePain(pain:Pain)

    @Delete
    fun deletePain(pain:Pain)

    @Query("select * from Pain")
    fun loadAllPain(): Flow<List<Pain>>

    @Query("select * from Pain where email = :email")
    fun loadUserPain(email: String): Flow<List<Pain>>

    @Query("select * from Pain where date = :date and email = :email")
    suspend fun check(date:String,email:String):Pain?

    @Query("select * from Pain where date = :date and email = :email")
    fun getPain(date: String,email: String):Pain

    /*@Query("select COUNT(location) as fre from Pain where email = :email and location = :location")
    fun locationCount(email: String,location:String): Int*/


}