package com.dscvit.werk.repository

import com.dscvit.werk.models.auth.SignUpRequest
import com.dscvit.werk.models.auth.SignUpResponse
import com.dscvit.werk.network.ApiClient
import com.dscvit.werk.network.ApiInterface
import com.dscvit.werk.util.Resource
import org.json.JSONObject
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient
) : AppRepository {

    override suspend fun signUpUser(signUpRequest: SignUpRequest) =
        apiClient.signUpUser(signUpRequest)

}