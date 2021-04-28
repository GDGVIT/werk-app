package com.dscvit.werk.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dscvit.werk.ui.overview.CompletedFragment
import com.dscvit.werk.ui.overview.OngoingFragment
import com.dscvit.werk.ui.overview.UpcomingFragment

class OverviewViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                UpcomingFragment()
            }
            1 -> {
                OngoingFragment()
            }
            2 -> {
                CompletedFragment()
            }
            else -> {
                Fragment()
            }
        }
    }
}