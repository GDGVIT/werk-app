package com.dscvit.werk.network

import com.dscvit.werk.models.auth.SignUpRequest
import javax.inject.Inject

class ApiClient @Inject constructor(
    private val api: ApiInterface
) : BaseApiClient() {

    suspend fun signUpUser(signUpRequest: SignUpRequest) = processResponse {
        api.signUpUser(signUpRequest)
    }

}