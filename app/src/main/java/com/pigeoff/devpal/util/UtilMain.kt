package com.pigeoff.devpal.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.pigeoff.devpal.DevpalApplication
import com.pigeoff.devpal.R
import com.pigeoff.devpal.database.mDatabase
import com.pigeoff.devpal.database.mDatabaseIssue
import kotlinx.android.synthetic.main.adapter_status.view.*

class UtilMain {

    val DATABASE_NAME: String = "devpaldatabase"

    //Intent Project Edit Class
    val ISSUE_ID: String = "issueid"
    val ISSUE_PARENT_ID = "issueparentid"
    val ISSUE_NO_PARENT = -1
    val ISSUE_NO_ID = -1

    //STATUS
    val ISSUE_STATUS_TO_DO: Int = 0
    val ISSUE_STATUS_IDEA: Int = 1
    val ISSUE_STATUS_IN_PROGRESS: Int = 2
    val ISSUE_STATUS_DONE: Int = 3
    val ISSUE_STATUS_ABANDONED: Int = 4

    //TYPE
    val ISSUE_TYPE: String = "issuetype"
    val ISSUE_TYPE_PROJECT: Int = 0
    val ISSUE_TYPE_BUG: Int = 1
    val ISSUE_TYPE_ENHANCE: Int = 2
    val ISSUE_TYPE_SOLUTION: Int = 3

    //Codes
    val REQUEST_CODE = 1871


    //Object manipulations
    fun isStrEmpty(str: String) : Boolean {
        var i = 0
        for (w in str) {
            if (w.toString() != " ") {
                i++
            }
        }
        return i == 0
    }

    //DB
    fun build(context: Context) : mDatabase {
        val app = context.applicationContext as DevpalApplication
        return app.getDatabase()
    }

    fun getIssuesOfParent(db: mDatabase, parent: Int) : MutableList<mDatabaseIssue> {
        val allIsues  = db.issueDAO().getFromIssue().toMutableList()
        var finalIssues = mutableListOf<mDatabaseIssue>()
        for (issue in allIsues) {
            if (issue.parent == parent) {
                finalIssues.add(issue)
            }
        }
        return finalIssues
    }


    //Interface
    fun setStatusWidget(context: Context, issue: mDatabaseIssue, statusWidget: TextView) {
        when (issue.status) {
            0 -> {
                statusWidget.setBackground(context.getDrawable(R.drawable.ic_cheep_red))
                statusWidget.text = context.getString(R.string.label_to_do)
                statusWidget.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
            }
            1 -> {
                statusWidget.setBackground(context.getDrawable(R.drawable.ic_cheep_blue))
                statusWidget.text = context.getString(R.string.label_idea)
                statusWidget.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))

            }
            2 -> {
                statusWidget.setBackground(context.getDrawable(R.drawable.ic_cheep_yellow))
                statusWidget.text = context.getString(R.string.label_in_progress)
                statusWidget.setTextColor(ContextCompat.getColor(context, android.R.color.black))
            }
            3 -> {
                statusWidget.setBackground(context.getDrawable(R.drawable.ic_cheep_green))
                statusWidget.text = context.getString(R.string.label_done)
                statusWidget.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))

            }
            4 -> {
                statusWidget.setBackground(context.getDrawable(R.drawable.ic_cheep_red))
                statusWidget.text = context.getString(R.string.label_abandoned)
                statusWidget.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))

            }
            else -> {
                statusWidget.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRed))
                statusWidget.text = context.getString(R.string.label_to_do)
                statusWidget.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
            }
        }
    }

    fun setMetroLine(imgDot: ImageView, lineTop: View, lineBottom: View, count: Int, position: Int, type: Int) {
        when {
            position == 0 -> {
                lineTop.alpha = 0.toFloat()
            }
            else -> {
                lineTop.alpha = 1.toFloat()
            }
        }
        when {
            (position == count - 1) -> {
                lineBottom.alpha = 0.toFloat()
            }
            else -> {
                lineBottom.alpha = 1.toFloat()
            }
        }

        if (type == ISSUE_TYPE_SOLUTION) {
            if(count == 1) {
                imgDot.visibility = View.GONE
                lineTop.visibility = View.GONE
                lineBottom.visibility = View.GONE
            }

            else {
                imgDot.visibility = View.VISIBLE
                lineTop.visibility = View.VISIBLE
                lineBottom.visibility = View.VISIBLE
            }
        }

        else {
            imgDot.visibility = View.GONE
            lineTop.visibility = View.GONE
            lineBottom.visibility = View.GONE
        }
    }
}