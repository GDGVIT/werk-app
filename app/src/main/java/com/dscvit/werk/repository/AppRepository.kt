package com.dscvit.werk.repository

import com.dscvit.werk.models.auth.*
import com.dscvit.werk.models.sessions.*
import com.dscvit.werk.models.task.TaskRequest
import com.dscvit.werk.models.task.TaskResponse
import com.dscvit.werk.util.Resource

interface AppRepository {
    suspend fun signUpUser(signUpRequest: SignUpRequest): Resource<SignUpResponse>

    suspend fun signInUser(signInRequest: SignInRequest): Resource<SignInResponse>

    suspend fun getSessions(): Resource<SessionsResponse>

    suspend fun createSession(createSessionRequest: CreateSessionRequest): Resource<CreateSessionResponse>

    suspend fun joinSession(joinSessionRequest: JoinSessionRequest): Resource<JoinSessionResponse>

    suspend fun sendVerificationEmail(sendVerificationRequest: SendVerificationRequest): Resource<Any>

    suspend fun getTasks(sessionID: Int): Resource<TaskResponse>

    fun saveJWTToken(token: String)

    fun getJWTToken(): String
}