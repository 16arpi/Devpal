package com.pigeoff.devpal.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.pigeoff.devpal.R
import com.pigeoff.devpal.adapters.ProjectPagerAdapter
import com.pigeoff.devpal.database.mDatabase
import com.pigeoff.devpal.database.mDatabaseIssue
import com.pigeoff.devpal.databinding.ActivityProjectBinding
import com.pigeoff.devpal.fragments.ProjectBugsFragment
import com.pigeoff.devpal.fragments.ProjectEnhanceFragment
import com.pigeoff.devpal.fragments.ProjectInfosFragment
import com.pigeoff.devpal.util.UtilMain
import com.pigeoff.devpal.util.UtilProject

class ProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectBinding
    private var projectId: Int = -1
    private var project: mDatabaseIssue = mDatabaseIssue()
    private lateinit var viewPager: ViewPager2
    private lateinit var db: mDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.projectAppBar)
        db = UtilMain().build(this)

        projectId = this.intent.getIntExtra(UtilMain().ISSUE_ID, UtilMain().ISSUE_NO_ID)
        project = db.issueDAO().getFromIssue(projectId)

        if (projectId != UtilMain().ISSUE_NO_ID) {
            supportActionBar?.title = project.title

            //val infosFragment = ProjectInfosFragment(projectId, db)
            val bugsFragment = ProjectBugsFragment(projectId, db)
            val enhanceFragment = ProjectEnhanceFragment(projectId, db)
            val tabsData = mutableListOf(bugsFragment, enhanceFragment)

            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            val tabs = binding.projectTabLayout
            viewPager = binding.projectViewPager
            viewPager.adapter = ProjectPagerAdapter(this, tabsData, projectId)

            TabLayoutMediator(tabs, viewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = getString(R.string.label_project_bugs)
                    }
                    1 -> {
                        tab.text = getString(R.string.label_project_enhance)
                    }
                }
            }.attach()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_project, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.finish()
            }
            R.id.action_add -> {
                val intent = Intent(this, IssueEditActivity::class.java)
                intent.putExtra(UtilMain().ISSUE_PARENT_ID, projectId)
                intent.putExtra(UtilMain().ISSUE_TYPE, getTypeFromViewpager(viewPager))
                this.startActivity(intent)
            }
            R.id.action_edit -> {
                val intent = Intent(this, IssueEditActivity::class.java)
                intent.putExtra(UtilMain().ISSUE_ID, projectId)
                this.startActivity(intent)
            }
            R.id.action_delete -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.label_delete)
                    .setMessage(R.string.prompt_delete)
                    .setNegativeButton(R.string.prompt_cancel, DialogInterface.OnClickListener { _, _ ->

                    })
                    .setPositiveButton(R.string.prompt_ok, DialogInterface.OnClickListener { _, _ ->
                        if (projectId != UtilMain().ISSUE_NO_ID) db.issueDAO().deleteIssuesFromParent(projectId)
                        db.issueDAO().deleteIssue(projectId)
                        this.finish()
                    })
                    .show()
            }
        }
        return true
    }

    fun getTypeFromViewpager(viewPager: ViewPager2) : Int {
        when (viewPager.currentItem) {
            0 -> {
                return UtilMain().ISSUE_TYPE_BUG
            }
            1 -> {
                return UtilMain().ISSUE_TYPE_ENHANCE
            }
            else -> {
                return UtilMain().ISSUE_TYPE_BUG
            }
        }
    }
}
