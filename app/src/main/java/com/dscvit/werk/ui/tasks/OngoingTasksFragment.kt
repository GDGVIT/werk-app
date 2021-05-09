package com.dscvit.werk.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.werk.databinding.FragmentOngoingTasksBinding
import com.dscvit.werk.ui.adapter.OngoingTasksAdapter

class OngoingTasksFragment : Fragment() {
    private var _binding: FragmentOngoingTasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOngoingTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = OngoingTasksAdapter()
        binding.ongoingTaskRecyclerView.adapter = adapter
        binding.ongoingTaskRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}