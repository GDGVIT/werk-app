package com.dscvit.werk.ui.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscvit.werk.models.auth.GoogleSignInRequest
import com.dscvit.werk.models.auth.SendVerificationRequest
import com.dscvit.werk.models.auth.SignInRequest
import com.dscvit.werk.models.auth.SignUpRequest
import com.dscvit.werk.repository.AppRepository
import com.dscvit.werk.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
    private val repository: AppRepository
) : ViewModel() {
    sealed class AuthEvent {
        object Success : AuthEvent()
        class Failure(val errorMessage: String, val statusCode: Int) : AuthEvent()
        object Loading : AuthEvent()
        object Initial : AuthEvent()
    }

    private val _signUpUser = MutableStateFlow<AuthEvent>(AuthEvent.Initial)
    val signUpUser: StateFlow<AuthEvent> = _signUpUser

    fun initSignUpUser(name: String, email: String, password: String) {
        val signUpRequest = SignUpRequest(email, name, password)

        viewModelScope.launch(Dispatchers.IO) {
            _signUpUser.value = AuthEvent.Loading
            when (val response = repository.signUpUser(signUpRequest)) {
                is Resource.Error -> _signUpUser.value =
                    AuthEvent.Failure(response.message!!, response.statusCode ?: -1)
                is Resource.Success -> {
                    repository.saveJWTToken(response.data!!.token)
                    repository.saveUserDetails(response.data.userDetails)
                    _signUpUser.value = AuthEvent.Success
                }
            }
        }
    }

    private val _signInUser = MutableStateFlow<AuthEvent>(AuthEvent.Initial)
    val signInUser: StateFlow<AuthEvent> = _signInUser

    fun initSignInUser(email: String, password: String) {
        val signInRequest = SignInRequest(email, password)

        viewModelScope.launch(Dispatchers.IO) {
            _signInUser.value = AuthEvent.Loading
            when (val response = repository.signInUser(signInRequest)) {
                is Resource.Error -> _signInUser.value =
                    AuthEvent.Failure(response.message!!, response.statusCode ?: -1)
                is Resource.Success -> {
                    repository.saveJWTToken(response.data!!.token)
                    repository.saveUserDetails(response.data.userDetails)
                    _signInUser.value = AuthEvent.Success
                }
            }
        }
    }

    private val _googleSignInUser = MutableStateFlow<AuthEvent>(AuthEvent.Initial)
    val googleSignInUser: StateFlow<AuthEvent> = _googleSignInUser

    fun initGoogleSignIn(idToken: String) {
        val googleSignInRequest = GoogleSignInRequest(idToken)

        viewModelScope.launch(Dispatchers.IO) {
            _googleSignInUser.value = AuthEvent.Loading
            when (val response = repository.googleSignIn(googleSignInRequest)) {
                is Resource.Error -> _googleSignInUser.value =
                    AuthEvent.Failure(response.message!!, response.statusCode ?: -1)
                is Resource.Success -> {
                    repository.saveJWTToken(response.data!!.token)
                    repository.saveUserDetails(response.data.userDetails)
                    _googleSignInUser.value = AuthEvent.Success
                }
            }
        }
    }

    fun resetGoogleSignIn() {
        _googleSignInUser.value = AuthEvent.Initial
    }

    fun sendVerificationEmail(email: String) {
        val sendVerificationRequest = SendVerificationRequest(email)
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendVerificationEmail(sendVerificationRequest)
        }
    }

    fun resetPassword(email: String) {
        val sendVerificationRequest = SendVerificationRequest(email)
        viewModelScope.launch(Dispatchers.IO) {
            repository.resetPassword(sendVerificationRequest)
        }
    }
}