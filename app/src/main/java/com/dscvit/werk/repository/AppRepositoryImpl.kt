package com.dscvit.werk.repository

import android.content.Context
import com.dscvit.werk.models.auth.SendVerificationRequest
import com.dscvit.werk.models.auth.SignInRequest
import com.dscvit.werk.models.auth.SignInResponse
import com.dscvit.werk.models.auth.SignUpRequest
import com.dscvit.werk.models.sessions.*
import com.dscvit.werk.network.ApiClient
import com.dscvit.werk.util.APP_PREF
import com.dscvit.werk.util.PREF_TOKEN
import com.dscvit.werk.util.PrefHelper
import com.dscvit.werk.util.PrefHelper.get
import com.dscvit.werk.util.PrefHelper.set
import com.dscvit.werk.util.Resource
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val context: Context,
) : AppRepository {

    override suspend fun signUpUser(signUpRequest: SignUpRequest) =
        apiClient.signUpUser(signUpRequest)

    override suspend fun signInUser(signInRequest: SignInRequest) =
        apiClient.signInUser(signInRequest)

    override suspend fun getSessions() = apiClient.getSessions()

    override suspend fun createSession(createSessionRequest: CreateSessionRequest) =
        apiClient.createSession(createSessionRequest)

    override suspend fun joinSession(joinSessionRequest: JoinSessionRequest) =
        apiClient.joinSession(joinSessionRequest)

    override suspend fun sendVerificationEmail(sendVerificationRequest: SendVerificationRequest) =
        apiClient.sendVerificationEmail(sendVerificationRequest)

    override fun saveJWTToken(token: String) {
        val sharedPrefs = PrefHelper.customPrefs(context, APP_PREF)
        sharedPrefs[PREF_TOKEN] = token
    }

    override fun getJWTToken(): String {
        val sharedPrefs = PrefHelper.customPrefs(context, APP_PREF)
        return sharedPrefs[PREF_TOKEN] ?: ""
    }

}