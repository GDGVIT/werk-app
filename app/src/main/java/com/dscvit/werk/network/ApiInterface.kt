package com.dscvit.werk.network

import com.dscvit.werk.models.auth.*
import com.dscvit.werk.models.participants.ParticipantsResponse
import com.dscvit.werk.models.sessions.*
import com.dscvit.werk.models.task.TaskRequest
import com.dscvit.werk.models.task.TaskResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @POST("/auth/verifyEmail")
    suspend fun sendVerificationEmail(@Body sendVerificationRequest: SendVerificationRequest): Response<Any>

    @GET("/task/session/{sessionID}")
    suspend fun getTasks(@Path("sessionID") sessionID: Int): Response<TaskResponse>

    @GET("/session/{sessionID}/participants")
    suspend fun getParticipants(@Path("sessionID") sessionID: Int): Response<ParticipantsResponse>
}