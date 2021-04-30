package com.dscvit.werk.ui.overview

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

    private val closeOverlay: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.close_overlay_anim
        )
    }

    private val openOverlay: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.open_overlay_anim
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

        binding.joinSessionFab.setOnClickListener {
            clicked = !clicked
            val action =
                SessionsOverviewFragmentDirections.actionSessionsOverviewFragmentToJoinSessionFragment()
            findNavController().navigate(action)
        }

        binding.createSessionFab.setOnClickListener {
            clicked = !clicked
            val action =
                SessionsOverviewFragmentDirections.actionSessionsOverviewFragmentToCreateSessionFragment()
            findNavController().navigate(action)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.createSession.visibility = View.VISIBLE
            binding.joinSession.visibility = View.VISIBLE
            binding.overlay.visibility = View.VISIBLE
        } else {
            binding.createSession.visibility = View.INVISIBLE
            binding.joinSession.visibility = View.INVISIBLE
            binding.overlay.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.createSession.startAnimation(fromBottom)
            binding.joinSession.startAnimation(fromBottom)
            binding.sessionFab.startAnimation(rotateOpen)
            binding.overlay.startAnimation(openOverlay)
        } else {
            binding.createSession.startAnimation(toBottom)
            binding.joinSession.startAnimation(toBottom)
            binding.sessionFab.startAnimation(rotateClose)
            binding.overlay.startAnimation(closeOverlay)
        }
    }
}