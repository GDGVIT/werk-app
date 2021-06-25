package com.dscvit.werk.ui.overview

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscvit.werk.models.sessions.Session
import com.dscvit.werk.models.sessions.SessionsResponse
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
}