package com.dscvit.werk.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.dscvit.werk.R
import com.dscvit.werk.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.accountExistsText.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding.logo to "logo_small")
            findNavController().navigate(
                R.id.action_welcomeFragment_to_signInFragment, null,
                null,
                extras
            )
        }

        binding.emailSignUpButton.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding.logo to "logo_small")
            findNavController().navigate(
                R.id.action_welcomeFragment_to_signUpFragment,
                null,
                null,
                extras
            )
        }
    }
}