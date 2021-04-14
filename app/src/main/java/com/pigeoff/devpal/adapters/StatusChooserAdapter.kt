package com.pigeoff.devpal.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pigeoff.devpal.R
import com.pigeoff.devpal.database.mDatabaseIssue
import com.pigeoff.devpal.util.UtilMain

class StatusChooserAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val types = listOf(0, 1, 2, 3, 4)

    private var mStatusChangeListener: OnStatusChangeListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return mViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_status, parent, false))
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as mViewHolder
        val issue = mDatabaseIssue()
        issue.status = position
        UtilMain().setStatusWidget(context, issue, holder.status)

        holder.linear.setOnClickListener {
            this.mStatusChangeListener?.onStatusChangeListener(issue.status)
        }
    }

    class mViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val linear = v.findViewById<LinearLayout>(R.id.sheetLinear)
        val status = v.findViewById<TextView>(R.id.sheetStatus)
    }

    interface OnStatusChangeListener {
        fun onStatusChangeListener(status: Int)
    }

    fun setOnStatusChangeListener(listener: OnStatusChangeListener) {
        this.mStatusChangeListener = listener
    }
}