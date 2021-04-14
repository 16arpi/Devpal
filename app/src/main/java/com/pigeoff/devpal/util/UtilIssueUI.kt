package com.pigeoff.devpal.util

import android.content.Context
import android.util.TypedValue
import android.view.View
import com.pigeoff.devpal.R
import com.pigeoff.devpal.adapters.IssueAdapter
import com.pigeoff.devpal.database.mDatabase
import com.pigeoff.devpal.database.mDatabaseIssue


class UtilIssueUI(issue: mDatabaseIssue) {

    private var issue = issue

    fun isFocusableAndClickable() : Boolean {
        return issue.type != 3
    }

    fun getBackgroundClickable(context: Context) : Int {
        val outValue = TypedValue()
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        return if (issue.type == 3) R.color.colorWhite else outValue.resourceId
    }

    fun getMenuVisibility() : Int {
        return if (issue.type == 0) View.GONE else View.VISIBLE
    }

    fun getDescriptionVisibility() : Int {
        return if (issue.type == 1 || issue.type == 2) View.GONE else View.VISIBLE
    }

    fun getStatusVisibility() : Int {
        return if (issue.type == 0) View.GONE else View.VISIBLE
    }

    fun getActionsVisibility() : Int {
        return if (issue.type != 3) View.GONE else View.VISIBLE
    }

    fun getStatusFromType(type: Int) : Int {
        return if (type >= 2) 1 else 0
    }

    fun getColorFromOriginalStatus(status: Int) : Int {
        return if (status >= 3) R.color.colorPrimary else R.color.colorWhiteDisabled
    }

    fun setIssueAccepted() {

    }

    fun changeIssueStatus(context: Context, status: Int, db: mDatabase, holder: IssueAdapter.ViewHolder) {
        issue.status = status
        db.issueDAO().updateIssue(issue)
        UtilMain().setStatusWidget(context, issue, holder.status)
    }
}