package com.dscvit.werk.ui.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscvit.werk.models.auth.SignUpRequest
import com.dscvit.werk.models.auth.SignUpResponse
import com.dscvit.werk.repository.AppRepository
import com.dscvit.werk.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
    private val repository: AppRepository
) : ViewModel() {
    sealed class SignUpEvent {
        class Success(val signUpResponse: SignUpResponse) : SignUpEvent()
        class Failure(val errorMessage: String) : SignUpEvent()
        object Loading : SignUpEvent()
        object Initial : SignUpEvent()
    }

    private val _signUpUser = MutableStateFlow<SignUpEvent>(SignUpEvent.Initial)
    val signUpUser: StateFlow<SignUpEvent> = _signUpUser

    fun initSignUpUser(name: String, email: String, password: String) {
        val signUpRequest = SignUpRequest(email, name, password)

        viewModelScope.launch(Dispatchers.IO) {
            _signUpUser.value = SignUpEvent.Loading
            when (val response = repository.signUpUser(signUpRequest)) {
                is Resource.Error -> _signUpUser.value = SignUpEvent.Failure(response.message!!)
                is Resource.Success -> _signUpUser.value = SignUpEvent.Success(response.data!!)
            }
        }
    }
}