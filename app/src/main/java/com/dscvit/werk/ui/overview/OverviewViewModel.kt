package com.dscvit.werk.ui.overview

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscvit.werk.models.sessions.*
import com.dscvit.werk.repository.AppRepository
import com.dscvit.werk.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

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

    private val _upcomingSession = MutableStateFlow<MutableList<Session>>(mutableListOf())
    val upcomingSessions: StateFlow<List<Session>> = _upcomingSession

    private val _ongoingSession = MutableStateFlow<MutableList<Session>>(mutableListOf())
    val ongoingSessions: StateFlow<List<Session>> = _ongoingSession

    private val _completedSession = MutableStateFlow<MutableList<Session>>(mutableListOf())
    val completedSessions: StateFlow<List<Session>> = _completedSession

    fun getSessions() {
        viewModelScope.launch(Dispatchers.IO) {
            _sessions.value = GetSessionsEvent.Loading

            _upcomingSession.value.clear()
            _ongoingSession.value.clear()
            _completedSession.value.clear()

            when (val response = appRepository.getSessions()) {
                is Resource.Error -> _sessions.value =
                    GetSessionsEvent.Failure(response.message ?: "", response.statusCode ?: -1)
                is Resource.Success -> {
                    if (response.data != null) {
                        val currTime = System.currentTimeMillis()
                        val currDate = Date(currTime)

                        response.data.sessions.forEach {
                            val startDate = Date(it.sessionDetails.startTime)
                            val endDate = Date(it.sessionDetails.endTime)

                            Log.d("BRR", "Start date: $startDate End date: $endDate")

                            // Upcoming Sessions
                            if (startDate.after(currDate)) {
                                _upcomingSession.value.add(it)
                            }

                            // Ongoing Sessions
                            if (startDate.before(currDate) && endDate.after(currDate)) {
                                _ongoingSession.value.add(it)
                            }

                            // Completed Sessions
                            if (endDate.before(currDate)) {
                                _completedSession.value.add(it)
                            }
                        }
                    }
                    _sessions.value = GetSessionsEvent.Success(response.data!!)
                }
            }
        }
    }

    sealed class CreateSessionEvent {
        data class Success(val createSessionResponse: CreateSessionResponse) : CreateSessionEvent()
        data class Failure(val errorMessage: String, val statusCode: Int) : CreateSessionEvent()
        object Loading : CreateSessionEvent()
        object Initial : CreateSessionEvent()
    }

    private val _createSession = MutableStateFlow<CreateSessionEvent>(CreateSessionEvent.Initial)
    val createSession: StateFlow<CreateSessionEvent> = _createSession

    fun createASession(createSessionRequest: CreateSessionRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            _createSession.value = CreateSessionEvent.Loading
            when (val response = appRepository.createSession(createSessionRequest)) {
                is Resource.Error -> _createSession.value =
                    CreateSessionEvent.Failure(response.message ?: "", response.statusCode ?: -1)
                is Resource.Success -> _createSession.value =
                    CreateSessionEvent.Success(response.data!!)
            }
        }
    }

    sealed class JoinSessionEvent {
        data class Success(val joinSessionResponse: JoinSessionResponse) : JoinSessionEvent()
        data class Failure(val errorMessage: String, val statusCode: Int) : JoinSessionEvent()
        object Loading : JoinSessionEvent()
        object Initial : JoinSessionEvent()
    }

    private val _joinSession = MutableStateFlow<JoinSessionEvent>(JoinSessionEvent.Initial)
    val joinSession: StateFlow<JoinSessionEvent> = _joinSession

    fun joinASession(joinSessionRequest: JoinSessionRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            _joinSession.value = JoinSessionEvent.Loading
            when (val response = appRepository.joinSession(joinSessionRequest)) {
                is Resource.Error -> _joinSession.value =
                    JoinSessionEvent.Failure(response.message ?: "", response.statusCode ?: -1)
                is Resource.Success -> _joinSession.value =
                    JoinSessionEvent.Success(response.data!!)
            }
        }
    }
}