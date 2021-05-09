package com.dscvit.werk.ui.session

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dscvit.werk.R
import com.dscvit.werk.databinding.FragmentSessionBinding
import com.dscvit.werk.ui.adapter.TasksViewPageAdapter
import com.google.android.material.tabs.TabLayoutMediator

class SessionFragment : Fragment() {
    private var _binding: FragmentSessionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TasksViewPageAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> {
                    tab.text = "UPCOMING"
                }
                1 -> {
                    tab.text = "ONGOING"
                }
                2 -> {
                    tab.text = "COMPLETED"
                }
            }
        }.attach()


    }
}