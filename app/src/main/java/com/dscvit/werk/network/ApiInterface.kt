package com.dscvit.werk.network

import com.dscvit.werk.models.auth.SignInRequest
import com.dscvit.werk.models.auth.SignInResponse
import com.dscvit.werk.models.auth.SignUpRequest
import com.dscvit.werk.models.auth.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("/auth/register")
    suspend fun signUpUser(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @POST("/auth/login")
    suspend fun signInUser(@Body signInRequest: SignInRequest): Response<SignInResponse>
}