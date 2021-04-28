package com.dscvit.werk.ui.overview

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.dscvit.werk.R
import com.dscvit.werk.databinding.FragmentSessionsOverviewBinding
import com.dscvit.werk.ui.adapter.OverviewViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class SessionsOverviewFragment : Fragment() {
    private var _binding: FragmentSessionsOverviewBinding? = null
    private val binding get() = _binding!!

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_open_anim
        )
    }

    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_close_anim
        )
    }

    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.from_bottom_anim
        )
    }

    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.to_bottom_anim
        )
    }

    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSessionsOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = OverviewViewPagerAdapter(childFragmentManager, lifecycle)
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

        binding.sessionFab.setOnClickListener {
            setVisibility(clicked)
            setAnimation(clicked)
            clicked = !clicked
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.createSessionFab.visibility = View.VISIBLE
            binding.joinSessionFab.visibility = View.VISIBLE
        } else {
            binding.createSessionFab.visibility = View.INVISIBLE
            binding.joinSessionFab.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.createSessionFab.startAnimation(fromBottom)
            binding.joinSessionFab.startAnimation(fromBottom)
            binding.sessionFab.startAnimation(rotateOpen)
        } else {
            binding.createSessionFab.startAnimation(toBottom)
            binding.joinSessionFab.startAnimation(toBottom)
            binding.sessionFab.startAnimation(rotateClose)
        }
    }
}