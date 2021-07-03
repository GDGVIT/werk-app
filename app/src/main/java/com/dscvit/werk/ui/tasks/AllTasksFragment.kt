package com.dscvit.werk.ui.tasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.werk.databinding.FragmentAllTasksBinding
import com.dscvit.werk.models.task.TempTask
import com.dscvit.werk.ui.adapter.AllTasksAdapter
import com.dscvit.werk.ui.overview.OverviewViewModel
import com.dscvit.werk.ui.session.SessionFragmentDirections
import com.dscvit.werk.ui.utils.OnItemClickListener
import com.dscvit.werk.ui.utils.addOnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AllTasksFragment : Fragment() {
    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    private val TAG: String = this.javaClass.simpleName

    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AllTasksAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

//        binding.recyclerView
//            .addOnItemClickListener(object : OnItemClickListener {
//            override fun onItemClicked(position: Int, view: View) {
//                val action =
//                    SessionFragmentDirections.actionSessionFragmentToTaskDescriptionActivity(tasks[position])
//                findNavController().navigate(action)
//            }
//        })

        lifecycleScope.launchWhenResumed {
            viewModel.tasks.collect { event ->
                when (event) {
                    is TaskViewModel.GetTasksEvent.Success -> {
                        Log.d(TAG, "All task success")
                        viewModel.allTasks.observe(viewLifecycleOwner, {
                            Log.d(TAG, "All Task: $it")
                            if (it.isEmpty()) {
                                binding.emptyText.visibility = View.VISIBLE
                                binding.recyclerView.visibility = View.GONE
                            } else {
                                binding.emptyText.visibility = View.GONE
                                adapter.setTasks(it)
                                binding.recyclerView.visibility = View.VISIBLE
                            }
                        })
                    }
                    is TaskViewModel.GetTasksEvent.Loading -> {
                        Log.d(TAG, "LOADING....")
                    }
                    is TaskViewModel.GetTasksEvent.Failure -> {
                    }
                    else -> {
                    }
                }
            }
        }
    }
}