package com.dscvit.werk.ui.tasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.werk.databinding.FragmentForYouTasksBinding
import com.dscvit.werk.ui.adapter.ForYouTasksAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ForYouTasksFragment : Fragment() {
    private var _binding: FragmentForYouTasksBinding? = null
    private val binding get() = _binding!!

    private val TAG: String = this.javaClass.simpleName

    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForYouTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ForYouTasksAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        lifecycleScope.launchWhenResumed {
            viewModel.tasks.collect { event ->
                when (event) {
                    is TaskViewModel.GetTasksEvent.Success -> {
                        Log.d(TAG, "For you task success")
                        viewModel.forYouTasks.observe(viewLifecycleOwner, {
                            Log.d(TAG, "For You Task: $it")
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