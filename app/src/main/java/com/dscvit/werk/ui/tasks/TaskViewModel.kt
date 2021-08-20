package com.dscvit.werk.ui.tasks

import android.os.Build
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.dscvit.werk.models.auth.UserDetails
import com.dscvit.werk.models.sessions.CreateSessionRequest
import com.dscvit.werk.models.sessions.CreateSessionResponse
import com.dscvit.werk.models.sessions.SessionDetails
import com.dscvit.werk.models.task.*
import com.dscvit.werk.repository.AppRepository
import com.dscvit.werk.util.Resource
import com.dscvit.werk.util.STATUS_COMPLETED
import com.dscvit.werk.util.STATUS_TERMINATED
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

class TaskViewModel @ViewModelInject constructor(
    private val appRepository: AppRepository
) : ViewModel() {
    lateinit var sessionDetails: SessionDetails
    val userDetails: UserDetails = appRepository.getUserDetails()

    sealed class GetTasksEvent {
        data class Success(val taskResponse: TaskResponse) : GetTasksEvent()
        data class Failure(val errorMessage: String, val statusCode: Int) : GetTasksEvent()
        object Loading : GetTasksEvent()
        object Initial : GetTasksEvent()
    }

    private val _tasks = MutableStateFlow<GetTasksEvent>(GetTasksEvent.Initial)
    val tasks: StateFlow<GetTasksEvent> = _tasks

    private val _allTasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    val allTasks: LiveData<MutableList<Task>> = _allTasks

    private val _forYouTasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    val forYouTasks: LiveData<MutableList<Task>> = _forYouTasks

    private val _completedTasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    val completedTasks: LiveData<MutableList<Task>> = _completedTasks

    fun getTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            _tasks.value = GetTasksEvent.Loading

            _allTasks.value?.clear()
            _forYouTasks.value?.clear()
            _completedTasks.value?.clear()

            Log.d("BRR", sessionDetails.sessionId.toString())

            when (val response = appRepository.getTasks(sessionDetails.sessionId)) {
                is Resource.Error -> {
                    _tasks.value =
                        GetTasksEvent.Failure(response.message ?: "", response.statusCode ?: -1)
                }
                is Resource.Success -> {
                    response.data?.tasks?.forEach {
                        // All Tasks
                        if (it.status != STATUS_COMPLETED && it.status != STATUS_TERMINATED) {
                            _allTasks.value?.add(it)
                        }

                        // Completed Tasks
                        if (it.status == STATUS_COMPLETED && it.status != STATUS_TERMINATED) {
                            _completedTasks.value?.add(it)
                        }

                        // For You Tasks
                        if (it.status != STATUS_COMPLETED && it.status != STATUS_TERMINATED) {
                            if (it.assignedTo == userDetails.userId) {
                                _forYouTasks.value?.add(it)
                            }
                        }
                    }

                    _tasks.value = GetTasksEvent.Success(response.data!!)
                }
            }
        }
    }

    fun removeTask(taskID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _allTasks.value?.forEach {
                if (it.taskId == taskID) {
                    _allTasks.value?.remove(it)
                }
            }

            _forYouTasks.value?.forEach {
                if (it.taskId == taskID) {
                    _forYouTasks.value?.remove(it)
                }
            }

            _completedTasks.value?.forEach {
                if (it.taskId == taskID) {
                    _completedTasks.value?.remove(it)
                }
            }
        }
    }

    sealed class CreateTaskEvent {
        data class Success(val task: Task) : CreateTaskEvent()
        data class Failure(val errorMessage: String, val statusCode: Int) : CreateTaskEvent()
        object Loading : CreateTaskEvent()
        object Initial : CreateTaskEvent()
    }

    private val _createTask = MutableStateFlow<CreateTaskEvent>(CreateTaskEvent.Initial)
    val createTask: StateFlow<CreateTaskEvent> = _createTask

    fun createATask(createTaskRequest: CreateTaskRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            _createTask.value = CreateTaskEvent.Loading

            createTaskRequest.sessionId = appRepository.getSessionID()
            when (val response = appRepository.createTask(createTaskRequest)) {
                is Resource.Error -> _createTask.value =
                    CreateTaskEvent.Failure(response.message ?: "", response.statusCode ?: -1)
                is Resource.Success -> _createTask.value =
                    CreateTaskEvent.Success(response.data!!)
            }
        }
    }

    sealed class ChangeStatusEvent {
        object Success : ChangeStatusEvent()
        data class Failure(val errorMessage: String, val statusCode: Int) : ChangeStatusEvent()
        object Loading : ChangeStatusEvent()
        object Initial : ChangeStatusEvent()
    }

    private val _terminateTask = MutableStateFlow<ChangeStatusEvent>(ChangeStatusEvent.Initial)
    val terminateTask: StateFlow<ChangeStatusEvent> = _terminateTask

    fun terminateTheTask(taskID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _terminateTask.value = ChangeStatusEvent.Loading

            when (val response = appRepository.terminateTask(taskID)) {
                is Resource.Error -> _terminateTask.value =
                    ChangeStatusEvent.Failure(response.message ?: "", response.statusCode ?: -1)
                is Resource.Success -> _terminateTask.value =
                    ChangeStatusEvent.Success
            }
        }
    }

    sealed class GetTaskDetailsEvent {
        data class Success(val taskDetailsResponse: TaskDetailsResponse) : GetTaskDetailsEvent()
        data class Failure(val errorMessage: String, val statusCode: Int) : GetTaskDetailsEvent()
        object Loading : GetTaskDetailsEvent()
        object Initial : GetTaskDetailsEvent()
    }

    private val _taskDetails = MutableStateFlow<GetTaskDetailsEvent>(GetTaskDetailsEvent.Initial)
    val taskDetails: StateFlow<GetTaskDetailsEvent> = _taskDetails

    fun getTaskDetails(taskID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _taskDetails.value = GetTaskDetailsEvent.Loading

            when (val response = appRepository.getTaskDetails(taskID)) {
                is Resource.Error -> _taskDetails.value =
                    GetTaskDetailsEvent.Failure(response.message ?: "", response.statusCode ?: -1)
                is Resource.Success -> _taskDetails.value =
                    GetTaskDetailsEvent.Success(response.data!!)
            }
        }
    }

    sealed class SubmitTaskEvent {
        object Success : SubmitTaskEvent()
        data class Failure(val errorMessage: String, val statusCode: Int) : SubmitTaskEvent()
        object Loading : SubmitTaskEvent()
        object Initial : SubmitTaskEvent()
    }

    private val _submitTask = MutableStateFlow<SubmitTaskEvent>(SubmitTaskEvent.Initial)
    val submitTask: StateFlow<SubmitTaskEvent> = _submitTask

    fun submitTask(taskID: Int, duration: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _submitTask.value = SubmitTaskEvent.Loading

            val submitRequest = SubmitRequest(duration)

            when (val response = appRepository.submitTask(taskID, submitRequest)) {
                is Resource.Error -> _submitTask.value =
                    SubmitTaskEvent.Failure(response.message ?: "", response.statusCode ?: -1)
                is Resource.Success -> _submitTask.value =
                    SubmitTaskEvent.Success
            }
        }
    }

    suspend fun startTask(taskID: Int) {
        val changeStatusRequest = ChangeStatusRequest(0)
        appRepository.changeTaskStatus(taskID, changeStatusRequest)
    }

    suspend fun pauseTask(taskID: Int) {
        val changeStatusRequest = ChangeStatusRequest(1)
        appRepository.changeTaskStatus(taskID, changeStatusRequest)
    }

    fun getUserID() = appRepository.getUserDetails().userId
}