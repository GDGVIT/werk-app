package com.dscvit.werk.ui.overview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscvit.werk.models.sessions.SessionsResponse
import com.dscvit.werk.repository.AppRepository
import com.dscvit.werk.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OverviewViewModel @ViewModelInject constructor(
    private val appRepository: AppRepository
) : ViewModel() {
    sealed class GetSessionsEvent {
        data class Success(val sessionsResponse: SessionsResponse) : GetSessionsEvent()
        data class Failure(val errorMessage: String, val statusCode: Int) : GetSessionsEvent()
        object Loading : GetSessionsEvent()
        object Initial : GetSessionsEvent()
    }

    private val _sessions = MutableStateFlow<GetSessionsEvent>(GetSessionsEvent.Initial)
    val sessions: StateFlow<GetSessionsEvent> = _sessions

    fun getSessions() {
        viewModelScope.launch(Dispatchers.IO) {
            _sessions.value = GetSessionsEvent.Loading
            when (val response = appRepository.getSessions()) {
                is Resource.Error -> _sessions.value =
                    GetSessionsEvent.Failure(response.message ?: "", response.statusCode ?: -1)
                is Resource.Success -> _sessions.value = GetSessionsEvent.Success(response.data!!)
            }
        }
    }
}