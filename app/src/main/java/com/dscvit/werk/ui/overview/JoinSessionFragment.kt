package com.dscvit.werk.ui.overview

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
import com.dscvit.werk.databinding.FragmentJoinSessionBinding
import com.dscvit.werk.models.sessions.JoinSessionRequest
import com.dscvit.werk.ui.utils.buildLoader
import com.dscvit.werk.ui.utils.showErrorSnackBar
import kotlinx.coroutines.flow.collect

class JoinSessionFragment : Fragment() {
    private val TAG: String = this.javaClass.simpleName

    private var _binding: FragmentJoinSessionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OverviewViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJoinSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.appBarTitle.text = requireContext().getString(R.string.join_a_session)
        binding.appBar.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.joinButton.setOnClickListener {
            if (binding.codeInput.editText!!.text.toString().isNotEmpty()) {
                viewModel.joinASession(JoinSessionRequest(binding.codeInput.editText!!.text.toString()))
            }

//            val action = JoinSessionFragmentDirections.actionJoinSessionFragmentToSessionActivity()
//            findNavController().navigate(action)
        }

        val loader = requireContext().buildLoader()

        lifecycleScope.launchWhenCreated {
            viewModel.joinSession.collect { event ->
                when (event) {
                    is OverviewViewModel.JoinSessionEvent.Loading -> {
                        Log.d(TAG, "LOADING....")
                        loader.show()
                    }
                    is OverviewViewModel.JoinSessionEvent.Failure -> {
                        Log.d(TAG, event.errorMessage)
                        loader.hide()
                        view.showErrorSnackBar(event.errorMessage)
                    }
                    is OverviewViewModel.JoinSessionEvent.Success -> {
                        loader.hide()
                        Log.d(TAG, "SESSION JOINED: ${event.joinSessionResponse}")
                    }
                    else -> {
                    }
                }
            }
        }
    }
}