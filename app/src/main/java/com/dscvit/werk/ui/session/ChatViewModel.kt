package com.dscvit.werk.ui.session

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.dscvit.werk.models.chat.ChatResponse
import com.dscvit.werk.models.chat.Message
import com.dscvit.werk.models.participants.ParticipantsResponse
import com.dscvit.werk.repository.AppRepository
import com.dscvit.werk.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel @ViewModelInject constructor(
    private val appRepository: AppRepository
) : ViewModel() {
    sealed class GetChatsEvent {
        data class Success(val chatResponse: ChatResponse) : GetChatsEvent()
        data class Failure(val errorMessage: String, val statusCode: Int) : GetChatsEvent()
        object Loading : GetChatsEvent()
        object Initial : GetChatsEvent()
    }

    private val _chats = MutableStateFlow<GetChatsEvent>(GetChatsEvent.Initial)
    val chats: StateFlow<GetChatsEvent> = _chats

    private val _chatMessages = MutableStateFlow<MutableList<Message>>(mutableListOf())
    val chatMessages: LiveData<List<Message>> = _chatMessages.asLiveData()

    fun getChats() {
        viewModelScope.launch(Dispatchers.IO) {
            _chats.value = GetChatsEvent.Loading

            when (val response = appRepository.getChats(appRepository.getSessionID())) {
                is Resource.Error -> {
                    _chats.value = GetChatsEvent.Failure(
                        response.message ?: "",
                        response.statusCode ?: -1
                    )
                }
                is Resource.Success -> {
                    _chats.value = GetChatsEvent.Success(response.data!!)
                    response.data.oldMessages.forEach {
                        _chatMessages.value.add(it)
                    }
                }
            }
        }
    }
}