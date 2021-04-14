package com.pigeoff.devpal.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pigeoff.devpal.R
import com.pigeoff.devpal.adapters.IssueAdapter
import com.pigeoff.devpal.adapters.StatusChooserAdapter
import com.pigeoff.devpal.callbacks.TaskDragDropCallback
import com.pigeoff.devpal.database.mDatabase
import com.pigeoff.devpal.database.mDatabaseIssue
import com.pigeoff.devpal.databinding.ActivityIssueBinding
import com.pigeoff.devpal.util.UtilIssueUI
import com.pigeoff.devpal.util.UtilMain
import com.pigeoff.devpal.util.UtilProject
import kotlinx.android.synthetic.main.adapter_issue.view.*
import kotlinx.android.synthetic.main.adapter_status.view.*
import kotlinx.android.synthetic.main.fragment_edit_issue.*

class IssueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIssueBinding
    private lateinit var db: mDatabase
    private lateinit var mAdapter: IssueAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var solutions: List<mDatabaseIssue>
    private var issueId = -1
    private var currentIssue = mDatabaseIssue()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIssueBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.issueToolbar)

        db = UtilMain().build(this)
        issueId = this.intent.getIntExtra(UtilMain().ISSUE_ID, UtilMain().ISSUE_NO_ID)
        currentIssue = db.issueDAO().getFromIssue(issueId)

        //Init Action Bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        //window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccentDark)

        //Init objet issue
        db = UtilMain().build(this)
        val issueId = this.intent.getIntExtra(UtilMain().ISSUE_ID, UtilMain().ISSUE_NO_ID)

        if (issueId != UtilMain().ISSUE_NO_ID) {
            currentIssue = db.issueDAO().getFromIssue(issueId)
            solutions = getSolutions(db)

            //Init UI
            binding.issueTitle.setText(currentIssue.title)
            binding.issueDescription.setText(currentIssue.description)
            if (currentIssue.description.isEmpty()) binding.issueDescription.visibility = View.GONE
            UtilMain().setStatusWidget(this, currentIssue, binding.status)

            //Init solutions
            val layoutManager = LinearLayoutManager(this)
            recyclerView = binding.issueRecycler
            mAdapter = IssueAdapter(this, db, solutions)

            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = mAdapter

            val taskTouchHelper = ItemTouchHelper(TaskDragDropCallback(mAdapter))
            taskTouchHelper.attachToRecyclerView(recyclerView)

            mAdapter.setOnStatusChangeListener(object : IssueAdapter.OnStatusChangeListener {
                override fun onStatusChangeListener() {
                    solutions = getSolutions(db)
                    mAdapter.update(solutions)
                }
            })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_activity_issue, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.finish()
            }
            R.id.action_label -> {
                showBottomSheetFragment()
            }
            R.id.action_add -> {
                val intent = Intent(this, IssueEditActivity::class.java)
                intent.putExtra(UtilMain().ISSUE_PARENT_ID, issueId)
                intent.putExtra(UtilMain().ISSUE_TYPE, UtilMain().ISSUE_TYPE_SOLUTION)
                this.startActivity(intent)
            }
            R.id.action_edit -> {
                val intent = Intent(this, IssueEditActivity::class.java)
                intent.putExtra(UtilMain().ISSUE_ID, issueId)
                this.startActivity(intent)
            }
            R.id.action_delete -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.label_delete)
                    .setMessage(R.string.prompt_delete)
                    .setNegativeButton(R.string.prompt_cancel, DialogInterface.OnClickListener { _, _ ->

                    })
                    .setPositiveButton(R.string.prompt_ok, DialogInterface.OnClickListener { _, _ ->
                        if (issueId != UtilMain().ISSUE_NO_ID) db.issueDAO().deleteIssuesFromParent(issueId)
                        db.issueDAO().deleteIssue(issueId)
                        this.finish()
                    })
                    .show()
            }
        }
        return true
    }

    fun showBottomSheetFragment() {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.status_bottom_sheet, null, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.statusBttmSheetRecycler)
        val sheetAdapter = StatusChooserAdapter(this)
        val linearManager = LinearLayoutManager(this)

        recyclerView.adapter = sheetAdapter
        recyclerView.layoutManager = linearManager

        dialog.setContentView(view)
        dialog.show()

        sheetAdapter.setOnStatusChangeListener(object: StatusChooserAdapter.OnStatusChangeListener {
            override fun onStatusChangeListener(status: Int) {
                currentIssue.status = status
                db.issueDAO().updateIssue(currentIssue)
                updateUI()
                dialog.dismiss()
            }
        })

    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    fun updateUI() {
        currentIssue = db.issueDAO().getFromIssue(issueId)
        binding.issueDescription.setText(currentIssue.description)
        binding.issueTitle.setText(currentIssue.title)
        UtilMain().setStatusWidget(this, currentIssue, binding.status)
        mAdapter.update(getSolutions(db))
    }

    fun getSolutions(db: mDatabase) : List<mDatabaseIssue> {
        val issues = db.issueDAO().getFromIssue()
        issues.toMutableList()
        val finalBugs = mutableListOf<mDatabaseIssue>()
        val allStatus = mutableListOf(2, 0, 1, 3, 4)
        for (s in allStatus) {
            for (elmt in issues) {
                if (elmt.type == UtilMain().ISSUE_TYPE_SOLUTION && elmt.parent == issueId && elmt.status == s)
                    finalBugs.add(elmt)
            }
        }
        return finalBugs
    }
}
