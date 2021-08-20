package com.dscvit.werk.models.sessions


import com.google.gson.annotations.SerializedName

data class JoinSessionRequest(
    @SerializedName("accessCode")
    val accessCode: String
)