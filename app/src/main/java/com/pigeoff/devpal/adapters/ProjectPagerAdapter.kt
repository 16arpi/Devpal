package com.pigeoff.devpal.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pigeoff.devpal.fragments.ProjectBugsFragment
import com.pigeoff.devpal.fragments.ProjectEnhanceFragment
import com.pigeoff.devpal.fragments.ProjectInfosFragment

class ProjectPagerAdapter(fm: FragmentActivity, tabs: MutableList<Fragment>, projectId: Int) : FragmentStateAdapter(fm) {

    private var tabs = tabs

    override fun createFragment(position: Int): Fragment {
        val tab = tabs.get(position)
        when (position) {
            0 -> {
                return tab as ProjectBugsFragment
            }

            1 -> {
                return tab as ProjectEnhanceFragment
            }

            else -> {
                return tab as ProjectInfosFragment
            }
        }
    }
    override fun getItemCount(): Int {
        return tabs.count()
    }
}