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
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.dscvit.werk.R
import com.dscvit.werk.databinding.FragmentSignUpBinding
import com.dscvit.werk.ui.utils.afterTextChanged
import com.dscvit.werk.ui.utils.buildLoader
import com.dscvit.werk.ui.utils.showErrorSnackBar
import com.dscvit.werk.ui.utils.showSuccessSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private val TAG: String = this.javaClass.simpleName

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    private var isEmailValid: Boolean = false
    private var isNameValid: Boolean = false
    private var isPasswordValid: Boolean = false

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

        binding.emailInput.editText?.afterTextChanged {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                binding.emailInput.error = null
                isEmailValid = true
            } else {
                binding.emailInput.error = "Enter a valid email"
                isEmailValid = false
            }
        }

        binding.nameInput.editText?.afterTextChanged {
            if (it.length < 3) {
                binding.nameInput.error = "Name must be at least 2 chars"
                isNameValid = false
            } else {
                binding.nameInput.error = null
                isNameValid = true
            }
        }

        binding.passwordInput.editText?.afterTextChanged {
            if (it.length < 6) {
                binding.passwordInput.error = "Password must be at least 5 chars"
                isPasswordValid = false
            } else {
                binding.passwordInput.error = null
                isPasswordValid = true
            }
        }

        binding.signUpButton.setOnClickListener {
            if (isEmailValid && isNameValid && isPasswordValid) {
                viewModel.initSignUpUser(
                    binding.nameInput.editText?.text.toString().trim(),
                    binding.emailInput.editText?.text.toString().trim(),
                    binding.passwordInput.editText?.text.toString().trim(),
                )
            }
        }

        val loader = requireContext().buildLoader()

        lifecycleScope.launchWhenStarted {
            viewModel.signUpUser.collect { event ->
                when (event) {
                    is AuthViewModel.AuthEvent.Success -> {
                        view.showSuccessSnackBar("Sign up successful! :)")
                        loader.hide()

                        val extras = FragmentNavigatorExtras(binding.logo to "app_bar_logo")
                        findNavController().navigate(
                            R.id.action_signUpFragment_to_sessionsOverviewFragment, null,
                            null,
                            extras
                        )
                    }
                    is AuthViewModel.AuthEvent.Failure -> {
                        Log.d(TAG, event.errorMessage)
                        view.showErrorSnackBar(event.errorMessage)
                        loader.hide()
                    }
                    AuthViewModel.AuthEvent.Loading -> {
                        Log.d(TAG, "SIGN UP IN PROGRESS...")
                        loader.show()
                    }
                    else -> {
                    }
                }
            }
        }
    }
}