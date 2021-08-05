package com.dscvit.werk.ui.overview

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.dscvit.werk.R
import com.dscvit.werk.databinding.FragmentSessionsOverviewBinding
import com.dscvit.werk.service.TimerService
import com.dscvit.werk.ui.adapter.OverviewViewPagerAdapter
import com.dscvit.werk.ui.utils.buildLoader
import com.dscvit.werk.util.APP_PREF
import com.dscvit.werk.util.PREF_TOKEN
import com.dscvit.werk.util.PrefHelper
import com.dscvit.werk.util.PrefHelper.set
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SessionsOverviewFragment : Fragment() {
    private val TAG: String = this.javaClass.simpleName

    private val viewModel: OverviewViewModel by activityViewModels()

    private var _binding: FragmentSessionsOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var statusReceiver: BroadcastReceiver

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

        val loader = requireContext().buildLoader()

        lifecycleScope.launchWhenCreated {
            viewModel.sessions.collect { event ->
                when (event) {
                    is OverviewViewModel.GetSessionsEvent.Success -> {
                        Log.d(TAG, event.sessionsResponse.toString())
                        binding.sessionRefresh.isRefreshing = false
                        binding.errorText.visibility = View.GONE
                        binding.viewPager.visibility = View.VISIBLE
                        loader.hide()
                    }
                    is OverviewViewModel.GetSessionsEvent.Loading -> {
                        Log.d(TAG, "LOADING....")
                        binding.errorText.visibility = View.GONE
                        binding.viewPager.visibility = View.GONE
                        loader.show()
                    }
                    is OverviewViewModel.GetSessionsEvent.Failure -> {
                        Log.d(TAG, event.errorMessage)
                        binding.errorText.visibility = View.VISIBLE
                        binding.viewPager.visibility = View.GONE
                        binding.errorText.text = event.errorMessage
                        loader.hide()
                        binding.sessionRefresh.isRefreshing = false
                    }
                    else -> {
                    }
                }
            }
        }

        binding.sessionRefresh.setOnRefreshListener {
            viewModel.getSessions()
        }

        val userDetails = viewModel.userDetails

        binding.profileButton.load(userDetails.avatar)

        binding.profileButton.setOnClickListener {
            val popup = PopupMenu(requireContext(), binding.profileButton)
            popup.menuInflater.inflate(R.menu.overview_profile_menu, popup.menu)

            popup.setOnMenuItemClickListener {
                if (it.itemId == R.id.sign_out) {
                    val timerService = Intent(requireActivity(), TimerService::class.java)
                    timerService.putExtra("TaskID", -1)
                    timerService.putExtra("TaskName", "")
                    timerService.putExtra("Action", TimerService.GET_TIMERS_STATUS)
                    requireActivity().startService(timerService)

                    return@setOnMenuItemClickListener true
                } else {
                    Toast.makeText(requireContext(), it.itemId.toString(), Toast.LENGTH_SHORT)
                        .show()
                    return@setOnMenuItemClickListener true
                }
            }

            popup.show()
        }

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

    override fun onStart() {
        super.onStart()
        viewModel.getSessions()

        // Receiving task status from service
        val statusFiler = IntentFilter()
        statusFiler.addAction("TimersStatus")
        statusReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val areTimersRunning = p1?.getBooleanExtra("AreTimersRunning", true)!!
                if (areTimersRunning) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Tasks in progress")
                        .setMessage("There are some tasks which are running, please pause them or complete them in order to sign out!")
                        .setPositiveButton("Cool") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                } else {
                    Log.d(TAG, "LOGOUT HERE")

                    val sharedPrefs = PrefHelper.customPrefs(requireContext(), APP_PREF)
                    sharedPrefs[PREF_TOKEN] = ""

                    val action =
                        SessionsOverviewFragmentDirections.actionSessionsOverviewFragmentToWelcomeFragment()
                    findNavController().navigate(action)
                }
            }
        }
        requireActivity().registerReceiver(statusReceiver, statusFiler)
    }

    override fun onPause() {
        super.onPause()

        requireActivity().unregisterReceiver(statusReceiver)
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