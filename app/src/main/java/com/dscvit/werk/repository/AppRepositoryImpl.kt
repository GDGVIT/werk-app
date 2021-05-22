package com.dscvit.werk.repository

import com.dscvit.werk.models.auth.SignUpRequest
import com.dscvit.werk.models.auth.SignUpResponse
import com.dscvit.werk.network.ApiInterface
import com.dscvit.werk.util.Resource
import org.json.JSONObject
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : AppRepository {
    override suspend fun signUpUser(signUpRequest: SignUpRequest): Resource<SignUpResponse> {
        return try {
            val response = api.signUpUser(signUpRequest)
            val statusCode = response.code()
            val result = response.body()
            if (response.isSuccessful && statusCode == 201 && result != null) {
                Resource.Success(result)
            } else {
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                Resource.Error(jsonObj.getString("error"))
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Oof something went wrong :(")
        }
    }
}