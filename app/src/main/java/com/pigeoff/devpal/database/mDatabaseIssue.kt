package com.pigeoff.devpal.database

import androidx.room.*

@Entity
data class mDatabaseIssue (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String? = null,
    var description: String = "",
    var pckg: String = "",
    var version: String = "",
    var color: Int = 0,
    var status: Int = 0,
    var type: Int = 0,
    var createDate: Int = 0,
    var lastModif: Int = 0,
    var parent: Int = -1,
    var emergency: Int = 0,
    var range: Int = 0
)