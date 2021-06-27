package com.dscvit.werk.network

import com.dscvit.werk.models.auth.SignInRequest
import com.dscvit.werk.models.auth.SignInResponse
import com.dscvit.werk.models.auth.SignUpRequest
import com.dscvit.werk.models.auth.SignUpResponse
import com.dscvit.werk.models.sessions.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @POST("/auth/register")
    suspend fun signUpUser(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @POST("/auth/login")
    suspend fun signInUser(@Body signInRequest: SignInRequest): Response<SignInResponse>

    @GET("/session")
    suspend fun getSessions(): Response<SessionsResponse>

    @POST("/session/create")
    suspend fun createSession(@Body createSessionRequest: CreateSessionRequest): Response<CreateSessionResponse>

    @POST("/session/join")
    suspend fun joinSession(@Body joinSessionRequest: JoinSessionRequest): Response<JoinSessionResponse>
}