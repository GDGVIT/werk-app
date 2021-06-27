package com.dscvit.werk.repository

import com.dscvit.werk.models.auth.SignInRequest
import com.dscvit.werk.models.auth.SignInResponse
import com.dscvit.werk.models.auth.SignUpRequest
import com.dscvit.werk.models.auth.SignUpResponse
import com.dscvit.werk.models.sessions.*
import com.dscvit.werk.util.Resource

interface AppRepository {
    suspend fun signUpUser(signUpRequest: SignUpRequest): Resource<SignUpResponse>

    suspend fun signInUser(signInRequest: SignInRequest): Resource<SignInResponse>

    suspend fun getSessions(): Resource<SessionsResponse>

    suspend fun createSession(createSessionRequest: CreateSessionRequest): Resource<CreateSessionResponse>

    suspend fun joinSession(joinSessionRequest: JoinSessionRequest): Resource<JoinSessionResponse>

    fun saveJWTToken(token: String)

    fun getJWTToken(): String
}