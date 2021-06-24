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
import com.dscvit.werk.databinding.FragmentSignInBinding
import com.dscvit.werk.ui.utils.afterTextChanged
import com.dscvit.werk.ui.utils.buildLoader
import com.dscvit.werk.ui.utils.showErrorSnackBar
import com.dscvit.werk.ui.utils.showSuccessSnackBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private val TAG: String = this.javaClass.simpleName

    private val viewModel: AuthViewModel by viewModels()

    private var isEmailValid: Boolean = false
    private var isPasswordValid: Boolean = false

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
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

        binding.passwordInput.editText?.afterTextChanged {
            if (it.length < 6) {
                binding.passwordInput.error = "Password must be at least 5 chars"
                isPasswordValid = false
            } else {
                binding.passwordInput.error = null
                isPasswordValid = true
            }
        }

        binding.signInButton.setOnClickListener {
            if (isEmailValid && isPasswordValid) {
                viewModel.initSignInUser(
                    binding.emailInput.editText?.text.toString(),
                    binding.passwordInput.editText?.text.toString(),
                )
            }
        }

        val loader = requireContext().buildLoader()

        lifecycleScope.launchWhenStarted {
            viewModel.signInUser.collect { event ->
                when (event) {
                    is AuthViewModel.AuthEvent.Success -> {
                        view.showSuccessSnackBar("Sign in successful! :)")
                        loader.hide()

                        val extras = FragmentNavigatorExtras(binding.logo to "app_bar_logo")
                        findNavController().navigate(
                            R.id.action_signInFragment_to_sessionsOverviewFragment, null,
                            null,
                            extras
                        )
                    }
                    is AuthViewModel.AuthEvent.Failure -> {
                        Log.d(TAG, event.errorMessage + event.statusCode)
                        if (event.statusCode == 401) {
                            // Show dialog for user to understand they need to verify their email
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Email Verification")
                                .setMessage("We have sent you a confirmation email, please use it to verify your email and try again 😉")
                                .setPositiveButton("Yep, Understood") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        } else {
                            view.showErrorSnackBar(event.errorMessage)
                        }
                        loader.hide()
                    }
                    AuthViewModel.AuthEvent.Loading -> {
                        Log.d(TAG, "SIGN IN IN PROGRESS...")
                        loader.show()
                    }
                    else -> {
                    }
                }
            }
        }
    }
}