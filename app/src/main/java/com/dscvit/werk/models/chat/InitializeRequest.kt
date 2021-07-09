package com.dscvit.werk.models.chat


import com.google.gson.annotations.SerializedName

data class InitializeRequest(
    @SerializedName("token")
    val userToken: String
)