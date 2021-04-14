package com.pigeoff.devpal.adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pigeoff.devpal.R
import com.pigeoff.devpal.activities.IssueActivity
import com.pigeoff.devpal.activities.IssueEditActivity
import com.pigeoff.devpal.activities.ProjectActivity
import com.pigeoff.devpal.database.mDatabase
import com.pigeoff.devpal.database.mDatabaseIssue
import com.pigeoff.devpal.util.UtilIssueUI
import com.pigeoff.devpal.util.UtilMain
import kotlinx.android.synthetic.main.adapter_issue.view.*
import java.util.*

class IssueAdapter(private val context: Context, private val db: mDatabase, private var issues: List<mDatabaseIssue>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mStatusChangeListener: OnStatusChangeListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_issue, parent, false))
    }

    override fun getItemCount(): Int {
        return issues.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        val currentIssue = issues.get(position)
        val util = UtilIssueUI(currentIssue)

        Log.i("Issue ID", currentIssue.title.toString())

        UtilMain().setMetroLine(holder.imgDot, holder.lineTop, holder.lineBottom, itemCount, position, currentIssue.type)
        holder.issueTitle.text = currentIssue.title
        holder.issueDescription.text = currentIssue.description
        holder.issueDescription.visibility = util.getDescriptionVisibility()
        UtilMain().setStatusWidget(context, currentIssue, holder.status)
        holder.status.visibility = util.getStatusVisibility()
        holder.imgMore.visibility = util.getMenuVisibility()
        holder.issueLayout.isClickable = util.isFocusableAndClickable()
        holder.issueLayout.isFocusable = util.isFocusableAndClickable()
        //holder.issueLayout.setBackgroundResource(util.getBackgroundClickable(context))

        if (currentIssue.description.isEmpty()) holder.issueDescription.visibility = View.GONE

        holder.issueLayout.setOnClickListener {
            val intent: Intent
            when (currentIssue.type) {
                0 -> {
                    intent = Intent(context, ProjectActivity::class.java)
                    intent.putExtra(UtilMain().ISSUE_ID, currentIssue.id)
                    context.startActivity(intent)
                }
                1 -> {
                    intent = Intent(context, IssueActivity::class.java)
                    intent.putExtra(UtilMain().ISSUE_ID, currentIssue.id)
                    context.startActivity(intent)
                }
                2 -> {
                    intent = Intent(context, IssueActivity::class.java)
                    intent.putExtra(UtilMain().ISSUE_ID, currentIssue.id)
                    context.startActivity(intent)
                }
                else -> {
                    showBottomSheetFragment(context, holder, position)
                }
            }
        }

        holder.imgMore.setOnClickListener {
            val popup = PopupMenu(context, it, Gravity.END)
            popup.inflate(R.menu.menu_issue)
            popup.show()

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_status -> {
                        showBottomSheetFragment(context, holder, position)
                    }
                    R.id.action_edit -> {
                        val intent = Intent(context, IssueEditActivity::class.java)
                        intent.putExtra(UtilMain().ISSUE_ID, currentIssue.id)
                        intent.putExtra(UtilMain().ISSUE_TYPE, currentIssue.type)
                        intent.putExtra(UtilMain().ISSUE_PARENT_ID, currentIssue.parent)
                        context.startActivity(intent)
                    }
                    R.id.action_delete -> {
                        MaterialAlertDialogBuilder(context)
                            .setTitle(R.string.label_delete)
                            .setMessage(R.string.prompt_delete)
                            .setNegativeButton(R.string.prompt_cancel, DialogInterface.OnClickListener { _, _ ->

                            })
                            .setPositiveButton(R.string.prompt_ok, DialogInterface.OnClickListener { _, _ ->
                                var newIssues = mutableListOf<mDatabaseIssue>()
                                for (elmt in issues) {
                                    if (elmt != currentIssue) {
                                        newIssues.add(elmt)
                                    }
                                }
                                if (currentIssue.id != UtilMain().ISSUE_NO_ID) db.issueDAO().deleteIssuesFromParent(currentIssue.id)
                                db.issueDAO().deleteIssue(currentIssue.id)
                                issues = newIssues
                                notifyDataSetChanged()
                            })
                            .show()
                    }
                }
                true
            }


        }

    }

    fun showBottomSheetFragment(context: Context, holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        val issue = issues.get(position)

        val dialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.status_bottom_sheet, null, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.statusBttmSheetRecycler)
        val sheetAdapter = StatusChooserAdapter(context)
        val linearManager = LinearLayoutManager(context)

        recyclerView.adapter = sheetAdapter
        recyclerView.layoutManager = linearManager

        dialog.setContentView(view)
        dialog.show()

        sheetAdapter.setOnStatusChangeListener(object: StatusChooserAdapter.OnStatusChangeListener {
            override fun onStatusChangeListener(status: Int) {
                UtilIssueUI(issue).changeIssueStatus(context, status, db, holder)
                mStatusChangeListener?.onStatusChangeListener()
                dialog.dismiss()
            }
        })

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val lineTop = v.lineTop
        val lineBottom = v.lineBottom
        val imgDot = v.imgDot
        val status = v.status
        val issueLayout = v.issueLayout
        val issueTitle = v.issueTitle
        val issueDescription = v.issueDescription
        val imgMore = v.imgMore
    }

    fun update(newIssues: List<mDatabaseIssue>) {
        issues = listOf<mDatabaseIssue>()
        issues = newIssues
        notifyDataSetChanged()
    }

    /* CODE VENU TOUT DROIT DE TO-DO */
    fun updateItemsRange(original: Int, target: Int) {
        Collections.swap(issues, original, target)
        notifyItemMoved(original, target)

        //Get IDs
        val taskOriginalId = issues.get(original).id
        val taskTargetId = issues.get(target).id

        //Assign new IDs
        issues.get(original).id = taskTargetId
        issues.get(target).id = taskOriginalId

        //Update database
        db.issueDAO().updateIssue(issues.get(original))
        db.issueDAO().updateIssue(issues.get(target))

    }

    fun getCurrentIssues() : List<mDatabaseIssue> {
        return issues
    }

    interface OnStatusChangeListener {
        fun onStatusChangeListener()
    }

    fun setOnStatusChangeListener(listener: IssueAdapter.OnStatusChangeListener) {
        this.mStatusChangeListener = listener
    }
}