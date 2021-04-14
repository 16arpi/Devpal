package com.pigeoff.devpal

import android.app.Application
import android.content.Intent
import android.content.ServiceConnection
import androidx.room.Room
import com.pigeoff.devpal.database.mDatabase
import java.util.concurrent.Executor

class DevpalApplication : Application() {

    val DATABASE_NAME: String = "devpaldatabase"

    private lateinit var db: mDatabase

    override fun onCreate() {
        super.onCreate()
        db = initDB()
    }

    override fun onTerminate() {
        super.onTerminate()
        db.close()
    }

    fun getDatabase() : mDatabase {
       return db
    }

    fun initDB() : mDatabase {
        return Room.databaseBuilder(
            this,
            com.pigeoff.devpal.database.mDatabase::class.java, DATABASE_NAME
        ).allowMainThreadQueries().build()
    }
}