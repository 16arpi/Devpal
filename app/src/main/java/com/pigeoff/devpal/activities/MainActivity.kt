package com.pigeoff.devpal.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.pigeoff.devpal.R
import com.pigeoff.devpal.adapters.IssueAdapter
import com.pigeoff.devpal.callbacks.TaskDragDropCallback
import com.pigeoff.devpal.database.mDatabase
import com.pigeoff.devpal.databinding.ActivityMainBinding
import com.pigeoff.devpal.util.UtilMain

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: mDatabase
    private lateinit var homeAdapter: IssueAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        db = UtilMain().build(this)
        val projects = UtilMain().getIssuesOfParent(db, UtilMain().ISSUE_NO_PARENT)
        val layoutManager = LinearLayoutManager(this)
        homeAdapter = IssueAdapter(this, db, projects)
        binding.homeRecyclerview.layoutManager = layoutManager
        binding.homeRecyclerview.adapter = homeAdapter

        val taskTouchHelper = ItemTouchHelper(TaskDragDropCallback(homeAdapter))
        taskTouchHelper.attachToRecyclerView(binding.homeRecyclerview)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                val intent = Intent(this, IssueEditActivity::class.java)
                intent.putExtra(UtilMain().ISSUE_TYPE, UtilMain().ISSUE_TYPE_PROJECT)
                startActivity(intent)
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        val projects = UtilMain().getIssuesOfParent(db, UtilMain().ISSUE_NO_PARENT)
        homeAdapter.update(projects)
    }
}
