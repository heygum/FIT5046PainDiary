package com.example.fit5046paindiary.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fit5046paindiary.entity.Pain
import com.example.fit5046paindiary.roomDao.PainDao

@Database(version = 1, entities = [Pain::class],exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun PainDao():PainDao

    companion object{

            private var instance: AppDatabase? = null

            @Synchronized
            fun getDatabase(context: Context): AppDatabase {
                instance?.let {
                    return it
                }
                return Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "app_database")
                    .build().apply {
                        instance = this
                    }
            }

    }
}