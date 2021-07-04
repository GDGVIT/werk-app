package com.dscvit.werk.ui.session

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dscvit.werk.R
import com.dscvit.werk.databinding.FragmentSessionBinding
import com.dscvit.werk.ui.adapter.TasksViewPageAdapter
import com.dscvit.werk.ui.overview.OverviewViewModel
import com.dscvit.werk.ui.tasks.TaskViewModel
import com.dscvit.werk.ui.utils.buildLoader
import com.dscvit.werk.ui.utils.showErrorSnackBar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SessionFragment : Fragment() {
    private val TAG: String = this.javaClass.simpleName

    private val viewModel: TaskViewModel by activityViewModels()

    private var _binding: FragmentSessionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loader = requireContext().buildLoader()

        lifecycleScope.launchWhenResumed {
            viewModel.tasks.collect { event ->
                when (event) {
                    is TaskViewModel.GetTasksEvent.Success -> {
                        Log.d(TAG, event.taskResponse.toString())
                        binding.taskRefresh.isRefreshing = false
                        binding.errorText.visibility = View.GONE
                        binding.viewPager.visibility = View.VISIBLE
                        loader.hide()
                    }
                    is TaskViewModel.GetTasksEvent.Loading -> {
                        Log.d(TAG, "LOADING....")
                        binding.errorText.visibility = View.GONE
                        binding.viewPager.visibility = View.GONE
                        loader.show()
                    }
                    is TaskViewModel.GetTasksEvent.Failure -> {
                        Log.d(TAG, event.errorMessage)
                        binding.errorText.visibility = View.VISIBLE
                        binding.viewPager.visibility = View.GONE
                        binding.errorText.text = event.errorMessage
                        loader.hide()
                        binding.taskRefresh.isRefreshing = false
                    }
                    else -> {
                    }
                }
            }
        }

        val adapter = TasksViewPageAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> {
                    tab.text = "ALL"
                }
                1 -> {
                    tab.text = "FOR YOU"
                }
                2 -> {
                    tab.text = "COMPLETED"
                }
            }
        }.attach()

        binding.taskRefresh.setOnRefreshListener {
            viewModel.getTasks()
        }

        binding.taskFab.setOnClickListener {
            if (viewModel.sessionDetails.taskCreationUniv || viewModel.sessionDetails.createdBy.userId == viewModel.userDetails.userId) {
                val action = SessionFragmentDirections.actionSessionFragmentToCreateTaskActivity()
                findNavController().navigate(action)
            } else {
                view.showErrorSnackBar("Only the host can create tasks 😕")
            }
        }

        viewModel.getTasks()
    }
}