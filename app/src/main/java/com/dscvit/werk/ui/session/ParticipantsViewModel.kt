package com.dscvit.werk.ui.session

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscvit.werk.models.participants.AssignRequest
import com.dscvit.werk.models.participants.Participant
import com.dscvit.werk.models.participants.ParticipantsResponse
import com.dscvit.werk.models.sessions.SessionDetails
import com.dscvit.werk.repository.AppRepository
import com.dscvit.werk.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ParticipantsViewModel @ViewModelInject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    sealed class GetParticipantsEvent {
        data class Success(val participantsResponse: ParticipantsResponse) : GetParticipantsEvent()
        data class Failure(val errorMessage: String, val statusCode: Int) : GetParticipantsEvent()
        object Loading : GetParticipantsEvent()
        object Initial : GetParticipantsEvent()
    }

    private val _participants = MutableStateFlow<GetParticipantsEvent>(GetParticipantsEvent.Initial)
    val participants: StateFlow<GetParticipantsEvent> = _participants

    fun getParticipants() {
        viewModelScope.launch(Dispatchers.IO) {
            _participants.value = GetParticipantsEvent.Loading

            when (val response = appRepository.getParticipants(appRepository.getSessionID())) {
                is Resource.Error -> {
                    _participants.value = GetParticipantsEvent.Failure(
                        response.message ?: "",
                        response.statusCode ?: -1
                    )
                }
                is Resource.Success -> {
                    _participants.value = GetParticipantsEvent.Success(response.data!!)
                }
            }
        }
    }

    sealed class AssignParticipantEvent {
        data class Success(val participant: Participant) : AssignParticipantEvent()
        data class Failure(val errorMessage: String, val statusCode: Int) : AssignParticipantEvent()
        object Loading : AssignParticipantEvent()
        object Initial : AssignParticipantEvent()
    }

    private val _assignParticipant =
        MutableStateFlow<AssignParticipantEvent>(AssignParticipantEvent.Initial)
    val assignParticipant: StateFlow<AssignParticipantEvent> = _assignParticipant

    fun assignAParticipant(participant: Participant, taskID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _assignParticipant.value = AssignParticipantEvent.Loading

            val assignRequest = AssignRequest(participant.userId)

            when (val response = appRepository.assignTask(assignRequest, taskID)) {
                is Resource.Error -> {
                    _assignParticipant.value = AssignParticipantEvent.Failure(
                        response.message ?: "",
                        response.statusCode ?: -1
                    )
                }
                is Resource.Success -> {
                    _assignParticipant.value = AssignParticipantEvent.Success(participant)
                }
            }
        }
    }
}