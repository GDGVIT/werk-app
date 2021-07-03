package com.dscvit.werk.ui.tasks

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dscvit.werk.models.sessions.SessionDetails
import com.dscvit.werk.models.task.Task
import com.dscvit.werk.models.task.TaskRequest
import com.dscvit.werk.models.task.TaskResponse
import com.dscvit.werk.repository.AppRepository
import com.dscvit.werk.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel @ViewModelInject constructor(
    private val appRepository: AppRepository
) : ViewModel() {
    lateinit var sessionDetails: SessionDetails

    sealed class GetTasksEvent {
        data class Success(val taskResponse: TaskResponse) : GetTasksEvent()
        data class Failure(val errorMessage: String, val statusCode: Int) : GetTasksEvent()
        object Loading : GetTasksEvent()
        object Initial : GetTasksEvent()
    }

    private val _tasks = MutableStateFlow<GetTasksEvent>(GetTasksEvent.Initial)
    val tasks: StateFlow<GetTasksEvent> = _tasks

    private val _allTasks = MutableStateFlow<MutableList<Task>>(mutableListOf())
    val allTasks: LiveData<List<Task>> = _allTasks.asLiveData()

    private val _forYouTasks = MutableStateFlow<MutableList<Task>>(mutableListOf())
    val forYouTasks: StateFlow<List<Task>> = _forYouTasks

    private val _completedTasks = MutableStateFlow<MutableList<Task>>(mutableListOf())
    val completedTasks: StateFlow<List<Task>> = _completedTasks

    fun getTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            _tasks.value = GetTasksEvent.Loading

            _allTasks.value.clear()
            _forYouTasks.value.clear()
            _completedTasks.value.clear()

            Log.d("BRR", sessionDetails.sessionId.toString())

            when (val response = appRepository.getTasks(sessionDetails.sessionId)) {
                is Resource.Error -> {
                    _tasks.value =
                        GetTasksEvent.Failure(response.message ?: "", response.statusCode ?: -1)
                }
                is Resource.Success -> {
                    Log.d("BRR", response.data.toString())

                    response.data?.tasks?.forEach {
                        // All Tasks
                        _allTasks.value.add(it)
                    }

                    _tasks.value = GetTasksEvent.Success(response.data!!)
                }
            }
        }
    }
}