package com.dscvit.werk.ui.auth

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dscvit.werk.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private val TAG: String = this.javaClass.simpleName;

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpButton.setOnClickListener {
            viewModel.initSignUpUser("vishal", "abc@gmail.com", "test1234")

//            val extras = FragmentNavigatorExtras(binding.logo to "app_bar_logo")
//            findNavController().navigate(
//                R.id.action_signUpFragment_to_sessionsOverviewFragment, null,
//                null,
//                extras
//            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.signUpUser.collect { event ->
                when (event) {
                    is AuthViewModel.SignUpEvent.Success -> {
                        Log.d(TAG, event.signUpResponse.toString())
                    }
                    is AuthViewModel.SignUpEvent.Failure -> {
                        Log.d(TAG, event.errorMessage)
                    }
                    AuthViewModel.SignUpEvent.Loading -> {
                        Log.d(TAG, "SIGN UP IN PROGRESS...")
                    }
                    else -> {
                    }
                }
            }
        }
    }
}