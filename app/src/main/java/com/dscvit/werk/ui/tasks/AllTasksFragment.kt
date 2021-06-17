package com.dscvit.werk.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.werk.databinding.FragmentAllTasksBinding
import com.dscvit.werk.models.task.TempTask
import com.dscvit.werk.ui.adapter.AllTasksAdapter
import com.dscvit.werk.ui.session.SessionFragmentDirections
import com.dscvit.werk.ui.utils.OnItemClickListener
import com.dscvit.werk.ui.utils.addOnItemClickListener

class AllTasksFragment : Fragment() {
    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    private val tasks = listOf(
        TempTask(id = 1, title = "Task 1", description = "Desc 1", isTaskStarted = false),
        TempTask(id = 2, title = "Task 2", description = "Desc 2", isTaskStarted = false),
        TempTask(id = 3, title = "Task 3", description = "Desc 3", isTaskStarted = false),
        TempTask(id = 4, title = "Task 4", description = "Desc 4", isTaskStarted = false),
    )

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
        adapter.setTasks(tasks)
        binding.upcomingTaskRecyclerView.adapter = adapter
        binding.upcomingTaskRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.upcomingTaskRecyclerView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val action =
                    SessionFragmentDirections.actionSessionFragmentToTaskDescriptionActivity(tasks[position])
                findNavController().navigate(action)
            }
        })
    }
}