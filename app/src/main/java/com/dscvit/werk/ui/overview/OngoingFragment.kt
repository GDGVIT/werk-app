package com.dscvit.werk.ui.overview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.werk.databinding.FragmentOngoingBinding
import com.dscvit.werk.ui.adapter.OngoingSessionsAdapter
import com.dscvit.werk.ui.utils.OnItemClickListener
import com.dscvit.werk.ui.utils.addOnItemClickListener
import com.dscvit.werk.ui.utils.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class OngoingFragment : Fragment() {
    private val TAG: String = this.javaClass.simpleName

    private val viewModel: OverviewViewModel by activityViewModels()

    private var _binding: FragmentOngoingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOngoingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = OngoingSessionsAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.recyclerView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val action =
                    SessionsOverviewFragmentDirections.actionSessionsOverviewFragmentToSessionActivity()
                findNavController().navigate(action)
            }
        })

        lifecycleScope.launchWhenCreated {
            viewModel.sessions.collect { event ->
                when (event) {
                    is OverviewViewModel.GetSessionsEvent.Success -> {
                        viewModel.ongoingSessions.collect {
                            Log.d(TAG, "Ongoing Sessions: $it")
                            if (it.isEmpty()) {
                                binding.emptyText.visibility = View.VISIBLE
                                binding.recyclerView.visibility = View.GONE
                            } else {
                                binding.emptyText.visibility = View.GONE
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