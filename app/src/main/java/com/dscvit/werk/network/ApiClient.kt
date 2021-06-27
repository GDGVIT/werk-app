package com.dscvit.werk.network

import com.dscvit.werk.models.auth.SendVerificationRequest
import com.dscvit.werk.models.auth.SignInRequest
import com.dscvit.werk.models.auth.SignUpRequest
import com.dscvit.werk.models.sessions.CreateSessionRequest
import com.dscvit.werk.models.sessions.JoinSessionRequest
import javax.inject.Inject

class ApiClient @Inject constructor(
    private val api: ApiInterface
) : BaseApiClient() {

    suspend fun signUpUser(signUpRequest: SignUpRequest) = processResponse {
        api.signUpUser(signUpRequest)
    }

    suspend fun signInUser(signInRequest: SignInRequest) = processResponse {
        api.signInUser(signInRequest)
    }

    suspend fun getSessions() = processResponse {
        api.getSessions()
    }

    suspend fun createSession(createSessionRequest: CreateSessionRequest) = processResponse {
        api.createSession(createSessionRequest)
    }

    suspend fun joinSession(joinSessionRequest: JoinSessionRequest) = processResponse {
        api.joinSession(joinSessionRequest)
    }

    suspend fun sendVerificationEmail(sendVerificationRequest: SendVerificationRequest) =
        processResponse {
            api.sendVerificationEmail(sendVerificationRequest)
        }

}