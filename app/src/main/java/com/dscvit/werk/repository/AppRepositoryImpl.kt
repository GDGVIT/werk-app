package com.dscvit.werk.repository

import android.content.Context
import com.dscvit.werk.models.auth.*
import com.dscvit.werk.models.participants.ParticipantsResponse
import com.dscvit.werk.models.sessions.*
import com.dscvit.werk.network.ApiClient
import com.dscvit.werk.util.*
import com.dscvit.werk.util.PrefHelper.get
import com.dscvit.werk.util.PrefHelper.set
import com.google.gson.Gson
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

    override suspend fun getTasks(sessionID: Int) = apiClient.getTasks(sessionID)

    override suspend fun getParticipants(sessionID: Int) = apiClient.getParticipants(sessionID)

    override fun saveJWTToken(token: String) {
        val sharedPrefs = PrefHelper.customPrefs(context, APP_PREF)
        sharedPrefs[PREF_TOKEN] = token
    }

    override fun getJWTToken(): String {
        val sharedPrefs = PrefHelper.customPrefs(context, APP_PREF)
        return sharedPrefs[PREF_TOKEN] ?: ""
    }

    override fun saveUserDetails(userDetails: UserDetails) {
        val sharedPrefs = PrefHelper.customPrefs(context, APP_PREF)
        val userDetailsStr = Gson().toJson(userDetails)
        sharedPrefs[PREF_USER_DETAILS] = userDetailsStr
    }

    override fun getUserDetails(): UserDetails {
        val sharedPrefs = PrefHelper.customPrefs(context, APP_PREF)
        val userDetailsStr = sharedPrefs[PREF_USER_DETAILS] ?: ""
        return Gson().fromJson(userDetailsStr, UserDetails::class.java)
    }

    override fun setSessionID(sessionID: Int) {
        val sharedPrefs = PrefHelper.customPrefs(context, APP_PREF)
        sharedPrefs[PREF_SESSION_ID] = sessionID
    }

    override fun getSessionID(): Int {
        val sharedPrefs = PrefHelper.customPrefs(context, APP_PREF)
        return sharedPrefs[PREF_SESSION_ID] ?: 0
    }

}