package com.dscvit.werk.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dscvit.werk.R
import com.dscvit.werk.databinding.FragmentInviteBinding

class InviteFragment : Fragment() {
    private var _binding: FragmentInviteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInviteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.appBarTitle.text = requireContext().getString(R.string.invite)
        binding.appBar.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.nextButton.setOnClickListener {
            val action = InviteFragmentDirections.actionInviteFragmentToSessionActivity()
            findNavController().navigate(action)
        }
    }
}