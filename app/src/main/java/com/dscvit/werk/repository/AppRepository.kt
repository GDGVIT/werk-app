package com.dscvit.werk.repository

import com.dscvit.werk.models.auth.*
import com.dscvit.werk.models.chat.ChatResponse
import com.dscvit.werk.models.participants.AssignRequest
import com.dscvit.werk.models.participants.ParticipantsResponse
import com.dscvit.werk.models.sessions.*
import com.dscvit.werk.models.task.*
import com.dscvit.werk.util.Resource

interface AppRepository {
    suspend fun signUpUser(signUpRequest: SignUpRequest): Resource<SignUpResponse>

    suspend fun signInUser(signInRequest: SignInRequest): Resource<SignInResponse>

    suspend fun googleSignIn(googleSignInRequest: GoogleSignInRequest): Resource<SignInResponse>

    suspend fun getSessions(): Resource<SessionsResponse>

    suspend fun createSession(createSessionRequest: CreateSessionRequest): Resource<CreateSessionResponse>

    suspend fun joinSession(joinSessionRequest: JoinSessionRequest): Resource<JoinSessionResponse>

    suspend fun sendVerificationEmail(sendVerificationRequest: SendVerificationRequest): Resource<Any>

    suspend fun resetPassword(sendVerificationRequest: SendVerificationRequest): Resource<Any>

    suspend fun getTasks(sessionID: Int): Resource<TaskResponse>

    suspend fun getParticipants(sessionID: Int): Resource<ParticipantsResponse>

    suspend fun createTask(createTaskRequest: CreateTaskRequest): Resource<Task>

    suspend fun assignTask(assignRequest: AssignRequest, taskID: Int): Resource<Any>

    suspend fun getChats(sessionID: Int): Resource<ChatResponse>

    suspend fun terminateTask(taskID: Int): Resource<Any>

    suspend fun changeTaskStatus(
        taskID: Int,
        changeStatusRequest: ChangeStatusRequest
    ): Resource<Any>

    suspend fun submitTask(taskID: Int, submitRequest: SubmitRequest): Resource<Any>

    suspend fun getTaskDetails(taskID: Int): Resource<TaskDetailsResponse>

    fun saveJWTToken(token: String)

    fun getJWTToken(): String

    fun saveUserDetails(userDetails: UserDetails)

    fun getUserDetails(): UserDetails

    fun setSessionID(sessionID: Int)

    fun getSessionID(): Int
}