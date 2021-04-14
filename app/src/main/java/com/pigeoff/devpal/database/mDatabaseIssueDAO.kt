package com.pigeoff.devpal.database

import androidx.room.*

@Dao
interface mDatabaseIssueDAO {
    @Query("SELECT * FROM mDatabaseIssue ORDER BY id DESC")
    fun getFromIssue() : List<mDatabaseIssue>

    @Query("SELECT * FROM mDatabaseIssue WHERE id LIKE :id")
    fun getFromIssue(id: Int) : mDatabaseIssue

    @Update
    fun updateIssue(project: mDatabaseIssue)

    @Insert
    fun addIssue(project: mDatabaseIssue)

    @Delete
    fun deleteIssue(project: mDatabaseIssue)

    @Query("DELETE FROM mDatabaseIssue WHERE id LIKE :id")
    fun deleteIssue(id: Int)

    @Query("DELETE FROM mDatabaseIssue WHERE parent LIKE :parent")
    fun deleteIssuesFromParent(parent: Int)
}