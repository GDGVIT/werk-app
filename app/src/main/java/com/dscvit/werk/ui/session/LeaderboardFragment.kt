package com.dscvit.werk.ui.session

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.dscvit.werk.databinding.FragmentLeaderboardBinding
import com.dscvit.werk.ui.utils.buildLoader
import kotlinx.coroutines.flow.collect

class LeaderboardFragment : Fragment() {
    private val TAG: String = this.javaClass.simpleName

    private var _binding: FragmentLeaderboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ParticipantsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loader = requireContext().buildLoader()

        lifecycleScope.launchWhenResumed {
            viewModel.participants.collect { event ->
                when (event) {
                    is ParticipantsViewModel.GetParticipantsEvent.Success -> {
                        Log.d(TAG, event.participantsResponse.user.toString())
                        loader.hide()
                        binding.profileImg.load(event.participantsResponse.user.avatar)
                        binding.rankText.text = "#${event.participantsResponse.rank}"
                        binding.totalPointsText.text =
                            event.participantsResponse.user.points.toString()
                    }
                    is ParticipantsViewModel.GetParticipantsEvent.Loading -> {
                        Log.d(TAG, "LOADING...")
                        loader.show()
                    }
                    is ParticipantsViewModel.GetParticipantsEvent.Failure -> {
                        loader.hide()
                        Log.d(TAG, event.errorMessage)
                    }
                    else -> {
                    }
                }
            }
        }

        viewModel.getParticipants()
    }
}