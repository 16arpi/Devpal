package com.pigeoff.devpal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pigeoff.devpal.R
import com.pigeoff.devpal.database.mDatabase

/**
 * A simple [Fragment] subclass.
 */
class ProjectInfosFragment(projectId: Int, db: mDatabase) : Fragment() {

    private val projectId = projectId

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TextView(activity).apply {
            setText("")
        }
    }

}
