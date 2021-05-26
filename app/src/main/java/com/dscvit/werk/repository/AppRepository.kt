package com.dscvit.werk.repository

import com.dscvit.werk.models.auth.SignInRequest
import com.dscvit.werk.models.auth.SignInResponse
import com.dscvit.werk.models.auth.SignUpRequest
import com.dscvit.werk.models.auth.SignUpResponse
import com.dscvit.werk.util.Resource

interface AppRepository {
    suspend fun signUpUser(signUpRequest: SignUpRequest): Resource<SignUpResponse>

    suspend fun signInUser(signInRequest: SignInRequest): Resource<SignInResponse>

    fun saveJWTToken(token: String)

    fun getJWTToken(): String
}