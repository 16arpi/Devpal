package com.pigeoff.devpal.database

import android.content.Context
import androidx.room.*

@Database(entities = arrayOf(mDatabaseIssue::class), version = 1)
abstract class mDatabase : RoomDatabase() {
    abstract fun issueDAO(): mDatabaseIssueDAO

    val DATABASE_NAME: String = "devpaldatabase"

}