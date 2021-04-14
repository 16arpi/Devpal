package com.pigeoff.devpal.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.pigeoff.devpal.database.mDatabase
import com.pigeoff.devpal.fragments.IssueEditFragment
import com.pigeoff.devpal.util.UtilMain

class IssueEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = buildDB(this)

        /*
            Il est nécessaire de récupérer 4 variables
            - issueId : id de l'issue, négatif si aucun id passé
            - issueParent : id du parent, négatif si aucun id est passé
            - issueType : type de l'issue, "project" par défault
         */
        val issueId = intent.getIntExtra(UtilMain().ISSUE_ID, UtilMain().ISSUE_NO_ID)
        val issueParent = intent.getIntExtra(UtilMain().ISSUE_PARENT_ID, UtilMain().ISSUE_NO_PARENT)
        val issueType = intent.getIntExtra(UtilMain().ISSUE_TYPE, UtilMain().ISSUE_TYPE_PROJECT)

        val editProjectFragment = IssueEditFragment(db, issueId, issueParent, issueType)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, editProjectFragment)
            .commit()
    }

    fun buildDB(context: Context) : mDatabase {
        return Room.databaseBuilder(
            context,
            mDatabase::class.java, UtilMain().DATABASE_NAME
        ).allowMainThreadQueries().build()
    }
}
