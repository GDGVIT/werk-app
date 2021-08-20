package com.dscvit.werk.models.auth


import com.google.gson.annotations.SerializedName

data class GoogleSignInRequest(
    @SerializedName("accessToken")
    val accessToken: String
)