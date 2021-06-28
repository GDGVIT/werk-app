package com.dscvit.werk.ui.overview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.werk.databinding.FragmentCompletedBinding
import com.dscvit.werk.ui.adapter.CompletedSessionsAdapter
import com.dscvit.werk.ui.utils.OnItemClickListener
import com.dscvit.werk.ui.utils.addOnItemClickListener
import com.dscvit.werk.ui.utils.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CompletedFragment : Fragment() {
    private val TAG: String = this.javaClass.simpleName

    private val viewModel: OverviewViewModel by activityViewModels()

    private var _binding: FragmentCompletedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CompletedSessionsAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.recyclerView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                view.showErrorSnackBar("Session already completed 😕")
            }
        })

        lifecycleScope.launchWhenCreated {
            viewModel.sessions.collect { event ->
                when (event) {
                    is OverviewViewModel.GetSessionsEvent.Success -> {
                        viewModel.completedSessions.collect {
                            Log.d(TAG, "Completed Sessions: $it")
                            if (it.isEmpty()) {
                                binding.emptyText.visibility = View.VISIBLE
                                binding.recyclerView.visibility = View.GONE
                            } else {
                                binding.emptyText.visibility = View.GONE
                                adapter.updateSessions(it)
                                binding.recyclerView.visibility = View.VISIBLE
                            }
                        }
                    }
                    is OverviewViewModel.GetSessionsEvent.Loading -> {
                        Log.d(TAG, "LOADING....")
                    }
                    is OverviewViewModel.GetSessionsEvent.Failure -> {
                    }
                    else -> {
                    }
                }
            }
        }
    }
}