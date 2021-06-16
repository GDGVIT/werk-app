package com.dscvit.werk.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dscvit.werk.ui.tasks.CompletedTasksFragment
import com.dscvit.werk.ui.tasks.ForYouTasksFragment
import com.dscvit.werk.ui.tasks.AllTasksFragment

class TasksViewPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AllTasksFragment()
            }
            1 -> {
                ForYouTasksFragment()
            }
            2 -> {
                CompletedTasksFragment()
            }
            else -> {
                Fragment()
            }
        }
    }
}