package com.pigeoff.devpal.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pigeoff.devpal.R
import com.pigeoff.devpal.adapters.IssueAdapter
import com.pigeoff.devpal.callbacks.TaskDragDropCallback
import com.pigeoff.devpal.database.mDatabase
import com.pigeoff.devpal.database.mDatabaseIssue
import com.pigeoff.devpal.util.UtilMain
import kotlinx.android.synthetic.main.activity_project_issues.view.*

/**
 * A simple [Fragment] subclass.
 */
class ProjectBugsFragment(parent: Int, db: mDatabase) : Fragment() {

    private val parent = parent
    private val db = db
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: IssueAdapter
    private lateinit var topLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_project_issues, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bugs = getBugs(db)

        topLabel = view.topLabel
        recyclerView = view.issueRecycler

        val label = this.getString(R.string.label_bugs)
        topLabel.setText(label)

        val layoutManager = LinearLayoutManager(requireContext())
        mAdapter = IssueAdapter(requireContext(), db, bugs)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter

        val taskTouchHelper = ItemTouchHelper(TaskDragDropCallback(mAdapter))
        taskTouchHelper.attachToRecyclerView(recyclerView)

        mAdapter.setOnStatusChangeListener(object : IssueAdapter.OnStatusChangeListener {
            override fun onStatusChangeListener() {
                val bugs = getBugs(db)
                mAdapter.update(bugs)
            }
        })

    }

    fun getBugs(db: mDatabase) : List<mDatabaseIssue> {
        val issues = db.issueDAO().getFromIssue()
        issues.toMutableList()
        val finalBugs = mutableListOf<mDatabaseIssue>()
        val allStatus = mutableListOf(2, 0, 1, 3, 4)
        for (s in allStatus) {
            for (elmt in issues) {
                if (elmt.type == UtilMain().ISSUE_TYPE_BUG && elmt.parent == parent && elmt.status == s)
                    finalBugs.add(elmt)
            }
        }
        return finalBugs
    }

    override fun onResume() {
        super.onResume()
        val bugs = getBugs(db)
        mAdapter.update(bugs)
    }

}
